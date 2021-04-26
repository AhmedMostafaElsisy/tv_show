package com.example.tv_show.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tv_show.network.ApiClient;
import com.example.tv_show.network.ApiService;
import com.example.tv_show.respones.TvShowDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TvShowDetailsRepository {

    private ApiService apiService;

    public TvShowDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowDetailsResponse> getTvShowDetails(String id) {
        MutableLiveData<TvShowDetailsResponse> data = new MutableLiveData<>();
        apiService.getTvShowsDetails(id).enqueue(new Callback<TvShowDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowDetailsResponse> call, @NonNull Response<TvShowDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowDetailsResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
