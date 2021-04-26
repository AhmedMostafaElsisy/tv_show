package com.example.tv_show.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tv_show.R;
import com.example.tv_show.databinding.ItemContainerSliderOmageBinding;

public class ImageSlierAdapter extends RecyclerView.Adapter<ImageSlierAdapter.ImageSliderViewHolder> {

    private String[] sliderImages;
    private LayoutInflater layoutInflater;

    public ImageSlierAdapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSliderOmageBinding sliderOmageBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_slider_omage, parent, false
        );
        return new ImageSliderViewHolder(sliderOmageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImages[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImages.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        private ItemContainerSliderOmageBinding itemContainerSliderOmageBinding;

        public ImageSliderViewHolder(ItemContainerSliderOmageBinding itemContainerSliderOmageBinding) {
            super(itemContainerSliderOmageBinding.getRoot());
            this.itemContainerSliderOmageBinding = itemContainerSliderOmageBinding;
        }

        public void bindSliderImage(String imageUrl) {
            itemContainerSliderOmageBinding.setImageURL(imageUrl);
        }
    }
}
