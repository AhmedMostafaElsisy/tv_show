package com.example.tv_show.network;

import com.example.tv_show.respones.TvShowDetailsResponse;
import com.example.tv_show.respones.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TvShowResponse> getMostPopularTvShows(@Query("page") int page);

    @GET("show-details")
    Call<TvShowDetailsResponse> getTvShowsDetails(@Query("q") String TvShowId);
}
