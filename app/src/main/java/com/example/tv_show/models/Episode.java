package com.example.tv_show.models;

import com.google.gson.annotations.SerializedName;

public class Episode {
    @SerializedName("name")
    private String name;
    @SerializedName("season")
    private String season;
    @SerializedName("episode")
    private String episode;
    @SerializedName("air_date")
    private String air_date;

    public String getName() {
        return name;
    }

    public String getSeason() {
        return season;
    }

    public String getEpisode() {
        return episode;
    }

    public String getAir_date() {
        return air_date;
    }
}
