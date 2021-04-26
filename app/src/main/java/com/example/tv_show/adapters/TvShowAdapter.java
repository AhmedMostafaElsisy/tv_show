package com.example.tv_show.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tv_show.R;
import com.example.tv_show.databinding.ItemTvShowBinding;
import com.example.tv_show.listener.TvShowsListener;
import com.example.tv_show.models.TvShow;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private List<TvShow> tvShows;
    private LayoutInflater layoutInflater;
    private TvShowsListener tvShowsListener;

    public TvShowAdapter(List<TvShow> tvShows, TvShowsListener tvShowsListener) {
        this.tvShows = tvShows;
        this.tvShowsListener = tvShowsListener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemTvShowBinding itemTvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_tv_show, parent, false
        );
        return new TvShowViewHolder(itemTvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        holder.bindTvShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private ItemTvShowBinding itemTvShowBinding;

        public TvShowViewHolder(ItemTvShowBinding itemTvShowBinding) {
            super(itemTvShowBinding.getRoot());
            this.itemTvShowBinding = itemTvShowBinding;
        }

        public void bindTvShow(TvShow tvShow) {
            itemTvShowBinding.setTvShow(tvShow);
            itemTvShowBinding.executePendingBindings();
            itemTvShowBinding.getRoot().setOnClickListener(view -> tvShowsListener.onTVShowClicked(tvShow));
        }
    }
}
