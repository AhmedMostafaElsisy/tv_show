package com.example.tv_show.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tv_show.R;
import com.example.tv_show.databinding.EpisodeItemBinding;
import com.example.tv_show.databinding.ItemTvShowBinding;
import com.example.tv_show.models.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder> {
    private LayoutInflater layoutInflater;

    private List<Episode> episodeList;

    public EpisodesAdapter(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public EpisodesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        EpisodeItemBinding episodeItemBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.episode_item, parent, false
        );
        return new EpisodesViewHolder(episodeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesViewHolder holder, int position) {
        holder.bindEpisode(episodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    static class EpisodesViewHolder extends RecyclerView.ViewHolder {
        private EpisodeItemBinding episodeItemBinding;

        public EpisodesViewHolder(EpisodeItemBinding episodeItemBinding) {
            super(episodeItemBinding.getRoot());
            this.episodeItemBinding = episodeItemBinding;
        }

        public void bindEpisode(Episode episode) {
            String title = "S";
            String season = episode.getSeason();
            if (season.length() == 1) {
                season = "0".concat(season);
            }
            String episodeNumber = episode.getEpisode();
            if (episodeNumber.length() == 1) {
                episodeNumber = "0".concat(episodeNumber);
            }
            episodeNumber = "E".concat(episodeNumber);
            title = title.concat(season).concat(episodeNumber);
            episodeItemBinding.setTitle(title);
            episodeItemBinding.setName(episode.getName());
            episodeItemBinding.setAirDate(episode.getAir_date());
        }
    }
}
