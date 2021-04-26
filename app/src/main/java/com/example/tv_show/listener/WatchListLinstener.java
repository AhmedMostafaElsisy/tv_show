package com.example.tv_show.listener;

import com.example.tv_show.models.TvShow;

public interface WatchListLinstener {
    void onTvShowClicked(TvShow tvShow);

    void removeTVShowFromWatchList(TvShow tvShow, int position);
}
