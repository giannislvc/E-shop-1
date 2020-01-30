package com.nativeboys.eshop.ui.main.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nativeboys.eshop.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ProductImageAdapter extends ListAdapter<String, ProductImageAdapter.ViewHolder> {

    private final static DiffUtil.ItemCallback<String> callback = new DiffUtil.ItemCallback<String>() {

        @Override
        public boolean areItemsTheSame(@NonNull String model, @NonNull String t1) {
            return model.equals(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String model, @NonNull String t1) {
            return model.equals(t1);
        }

    };

    ProductImageAdapter() {
        super(callback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = getItem(position);
        if (url != null) holder.bind(url);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageHolder;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHolder = itemView.findViewById(R.id.image_holder);
        }

        private void bind(@NonNull String url) {
            Glide.with(imageHolder.getContext())
                    .load(url)
                    .transform(new CenterCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageHolder);
        }

    }

}
