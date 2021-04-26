package com.example.tv_show.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tv_show.R;
import com.example.tv_show.adapters.TvShowAdapter;
import com.example.tv_show.databinding.ActivityMainBinding;
import com.example.tv_show.listener.TvShowsListener;
import com.example.tv_show.models.TvShow;
import com.example.tv_show.viewModels.MostPopularTvShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TvShowsListener {
    private MostPopularTvShowViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TvShow> tvShows = new ArrayList<>();
    private TvShowAdapter tvShowAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        daInitialization();
    }

    private void daInitialization() {
        activityMainBinding.RecycleViewTvShow.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTvShowViewModel.class);
        tvShowAdapter = new TvShowAdapter(tvShows, this);
        activityMainBinding.RecycleViewTvShow.setAdapter(tvShowAdapter);
        activityMainBinding.RecycleViewTvShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.RecycleViewTvShow.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTvShows();
                    }
                }
            }
        });
        activityMainBinding.imageWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WatchListActivity.class);
                startActivity(intent);
            }
        });
        getMostPopularTvShows();
    }

    private void getMostPopularTvShows() {
        toggleLoading();
        viewModel.getMostPopularTvShow(currentPage).observe(this, mostPopularTvShowRepository -> {
            toggleLoading();
            if (mostPopularTvShowRepository != null) {
                totalAvailablePages = mostPopularTvShowRepository.getPages();
                if (mostPopularTvShowRepository.getTv_shows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTvShowRepository.getTv_shows());
                    tvShowAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }

        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            activityMainBinding.setIsLoading(activityMainBinding.getIsLoading() == null || !activityMainBinding.getIsLoading());
        } else {
            activityMainBinding.setIsLoadingMore(activityMainBinding.getIsLoadingMore() == null || !activityMainBinding.getIsLoadingMore());
        }
    }

    @Override
    public void onTVShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), tvShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}