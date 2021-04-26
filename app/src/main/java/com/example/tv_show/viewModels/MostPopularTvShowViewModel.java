package com.example.tv_show.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tv_show.repositories.MostPopularTvShowRepository;
import com.example.tv_show.respones.TvShowResponse;

public class MostPopularTvShowViewModel extends ViewModel {
    MostPopularTvShowRepository mostPopularTvShowRepository;

    public MostPopularTvShowViewModel() {
        mostPopularTvShowRepository = new MostPopularTvShowRepository();
    }

    public LiveData<TvShowResponse> getMostPopularTvShow(int page) {
        return mostPopularTvShowRepository.getMostPopularTvShow(page);
    }
}
