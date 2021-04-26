package com.example.tv_show.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tv_show.network.ApiClient;
import com.example.tv_show.network.ApiService;
import com.example.tv_show.respones.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTvShowRepository {
    private ApiService apiService;

    public MostPopularTvShowRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowResponse> getMostPopularTvShow(int page) {
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTvShows(page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
