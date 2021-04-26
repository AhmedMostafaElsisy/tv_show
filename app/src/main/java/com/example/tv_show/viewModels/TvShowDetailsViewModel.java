package com.example.tv_show.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tv_show.database.TVShowDataBase;
import com.example.tv_show.models.TvShow;
import com.example.tv_show.repositories.TvShowDetailsRepository;
import com.example.tv_show.respones.TvShowDetailsResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TvShowDetailsViewModel extends AndroidViewModel {
    private TvShowDetailsRepository tvShowDetailsRepository;
    private TVShowDataBase tvShowDataBase;

    public TvShowDetailsViewModel(@NonNull Application application) {

        super(application);
        tvShowDetailsRepository = new TvShowDetailsRepository();
        tvShowDataBase = TVShowDataBase.getTvShowDataBase(application);
    }

    public LiveData<TvShowDetailsResponse> getTvShowDetails(String id) {
        return tvShowDetailsRepository.getTvShowDetails(id);
    }

    public Completable addToWatchList(TvShow tvShow) {
        return tvShowDataBase.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TvShow> getTVShowFromWatchList(String id) {
        return tvShowDataBase.tvShowDao().getTVShowFromWatchList(id);
    }

    public Completable removeTVShowFromWatchList(TvShow tvShow) {
        return tvShowDataBase.tvShowDao().removeFromWatchList(tvShow);
    }

}
