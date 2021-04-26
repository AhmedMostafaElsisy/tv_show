package com.example.tv_show.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tv_show.R;
import com.example.tv_show.adapters.EpisodesAdapter;
import com.example.tv_show.adapters.ImageSlierAdapter;
import com.example.tv_show.adapters.WatchListAdapter;
import com.example.tv_show.databinding.ActivityTvShowDetailsBinding;
import com.example.tv_show.databinding.LayoutEpisodesButtomSheetBinding;
import com.example.tv_show.models.TvShow;
import com.example.tv_show.utlities.TempDataHolder;
import com.example.tv_show.viewModels.TvShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class tvShowDetailsActivity extends AppCompatActivity {
    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TvShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesButtomSheetBinding layoutEpisodesButtomSheetBinding;
    private TvShow tvShow;
    private Boolean isTVShowAvailableInWatchList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details);

        daInitialization();
    }

    private void daInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        activityTvShowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        tvShow = (TvShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchList();
        getTvShowDetails();
    }

    private void checkTVShowInWatchList() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchList(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    isTVShowAvailableInWatchList = true;
                    activityTvShowDetailsBinding.fabWatchList.setImageDrawable(ContextCompat.getDrawable(tvShowDetailsActivity.this, R.drawable.ic_add));
                    compositeDisposable.dispose();
                }));
    }

    private void getTvShowDetails() {
        activityTvShowDetailsBinding.setIsLoading(true);
        String tvShowID = String.valueOf(tvShow.getId());

        tvShowDetailsViewModel.getTvShowDetails(tvShowID).observe(this, tvShowDetailsResponse -> {
            activityTvShowDetailsBinding.setIsLoading(false);
            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                activityTvShowDetailsBinding.setTvShowImageUrl(
                        tvShowDetailsResponse.getTvShowDetails().getImage_path()
                );
                activityTvShowDetailsBinding.imageTvShow.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.setDescription(
                        String.valueOf(HtmlCompat.fromHtml(
                                tvShowDetailsResponse.getTvShowDetails().getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY
                        ))
                );
                activityTvShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.textReadMore.setOnClickListener(view -> {
                    if (activityTvShowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                        activityTvShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                        activityTvShowDetailsBinding.textDescription.setEllipsize(null);
                        activityTvShowDetailsBinding.textReadMore.setText(R.string.readLess);
                    } else {
                        activityTvShowDetailsBinding.textReadMore.setText(R.string.read_more);
                        activityTvShowDetailsBinding.textDescription.setMaxLines(4);
                        activityTvShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                    }
                });
                activityTvShowDetailsBinding.setRating(String.format(
                        Locale.getDefault(), "%.2f", Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                ));
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                    activityTvShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                } else {
                    activityTvShowDetailsBinding.setGenre("N/A");
                }
                activityTvShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                activityTvShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.BtnWebsite.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });
                activityTvShowDetailsBinding.BtnEpisodes.setOnClickListener(view -> {
                    if (episodesBottomSheetDialog == null) {
                        episodesBottomSheetDialog = new BottomSheetDialog(tvShowDetailsActivity.this);
                        layoutEpisodesButtomSheetBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(tvShowDetailsActivity.this),
                                R.layout.layout_episodes_buttom_sheet,
                                findViewById(R.id.episodesContainer), false
                        );
                        episodesBottomSheetDialog.setContentView(layoutEpisodesButtomSheetBinding.getRoot());
                        layoutEpisodesButtomSheetBinding.episodesRecycler.setAdapter(new EpisodesAdapter(
                                tvShowDetailsResponse.getTvShowDetails().getEpisodes()
                        ));
                        layoutEpisodesButtomSheetBinding.textTitle.setText(
                                String.format("Episodes | %s", tvShow.getName())
                        );
                        layoutEpisodesButtomSheetBinding.imageClose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());

                    }
                    episodesBottomSheetDialog.show();
                });
                activityTvShowDetailsBinding.BtnEpisodes.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.BtnWebsite.setVisibility(View.VISIBLE);
                activityTvShowDetailsBinding.fabWatchList.setOnClickListener(view -> {

                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if (isTVShowAvailableInWatchList) {
                        compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchList(tvShow)

                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {

                                    isTVShowAvailableInWatchList = false;
                                    TempDataHolder.tempo_data_updated = true;
                                    activityTvShowDetailsBinding.fabWatchList.setImageDrawable(ContextCompat.getDrawable(tvShowDetailsActivity.this, R.drawable.ic_eye));
                                    Toast.makeText(tvShowDetailsActivity.this, "remove from watch list", Toast.LENGTH_SHORT).show();

                                    compositeDisposable.dispose();
                                })
                        );


                    } else {
                        compositeDisposable.add(tvShowDetailsViewModel.addToWatchList(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    TempDataHolder.tempo_data_updated = true;
                                    activityTvShowDetailsBinding.fabWatchList.setImageDrawable(ContextCompat.getDrawable(tvShowDetailsActivity.this, R.drawable.ic_add));
                                    Toast.makeText(tvShowDetailsActivity.this, "add to watch list", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                })
                        );
                    }
                });
                activityTvShowDetailsBinding.fabWatchList.setVisibility(View.VISIBLE);
                loadBasicTvShowDetails();
            }
        });

    }

    private void loadImageSlider(String[] images) {
        activityTvShowDetailsBinding.imageSlider.setOffscreenPageLimit(1);
        activityTvShowDetailsBinding.imageSlider.setAdapter(new ImageSlierAdapter(images));
        activityTvShowDetailsBinding.imageSlider.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setUpSliderIndicators(images.length);
        activityTvShowDetailsBinding.imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setUpSliderIndicators(int count) {

        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.background_slider_indacator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            activityTvShowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvShowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int pos) {
        int childCount = activityTvShowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvShowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == pos) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indocator_active));

            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indacator_inactive));
            }
        }
    }

    private void loadBasicTvShowDetails() {
        activityTvShowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvShowDetailsBinding.setTvShowCountry(tvShow.getNetwork() + " (" + tvShow.getCountry() + ")");
        activityTvShowDetailsBinding.setTvShowStartDate(tvShow.getStart_date());
        activityTvShowDetailsBinding.setTvShowStatus(tvShow.getStatus());
        activityTvShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textNetwork.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStartDate.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);


    }
}