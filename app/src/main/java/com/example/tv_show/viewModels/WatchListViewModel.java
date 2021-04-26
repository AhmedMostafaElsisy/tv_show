package com.example.tv_show.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tv_show.database.TVShowDataBase;
import com.example.tv_show.models.TvShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchListViewModel extends AndroidViewModel {

    private TVShowDataBase tvShowDataBase;

    public WatchListViewModel(@NonNull Application application) {
        super(application);
        tvShowDataBase = TVShowDataBase.getTvShowDataBase(application);
    }

    public Flowable<List<TvShow>> loadWatchlist() {
        return tvShowDataBase.tvShowDao().getWatchList();
    }

    public Completable removeFromWatchList(TvShow tvShow) {
        return tvShowDataBase.tvShowDao().removeFromWatchList(tvShow);
    }

}
