package com.example.tv_show.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tv_show.R;
import com.example.tv_show.adapters.WatchListAdapter;
import com.example.tv_show.databinding.ActivityWatchListBinding;
import com.example.tv_show.listener.WatchListLinstener;
import com.example.tv_show.models.TvShow;
import com.example.tv_show.utlities.TempDataHolder;
import com.example.tv_show.viewModels.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchListLinstener {
    private ActivityWatchListBinding activityWatchListBinding;
    private WatchListViewModel watchListViewModel;
    private WatchListAdapter watchListAdapter;
    private List<TvShow> watchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);

        daInitialization();
    }

    private void daInitialization() {
        watchListViewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(view -> {
            onBackPressed();
        });
        watchList = new ArrayList<>();
        loadWatchList();
    }

    private void loadWatchList() {
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchListBinding.setIsLoading(false);
                    if (watchList.size() > 0) {
                        watchList.clear();
                    }
                    watchList.addAll(tvShows);
                    watchListAdapter = new WatchListAdapter(watchList, this);
                    activityWatchListBinding.watchListRecycle.setAdapter(watchListAdapter);
                    activityWatchListBinding.watchListRecycle.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.tempo_data_updated) {
            loadWatchList();
            TempDataHolder.tempo_data_updated = false;
        }

    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), tvShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchList(TvShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.removeFromWatchList(tvShow)

                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchList.remove(position);
                    watchListAdapter.notifyItemRemoved(position);
                    watchListAdapter.notifyItemRangeChanged(position, watchListAdapter.getItemCount());
                    compositeDisposable.dispose();
                })
        );
    }
}