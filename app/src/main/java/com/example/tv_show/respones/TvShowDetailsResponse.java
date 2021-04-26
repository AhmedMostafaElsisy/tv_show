package com.example.tv_show.respones;

import com.example.tv_show.models.TvShowDetails;
import com.google.gson.annotations.SerializedName;

public class TvShowDetailsResponse {

    @SerializedName("tvShow")
    private TvShowDetails tvShowDetails;

    public TvShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
