package com.nativeboys.eshop.ui.main.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.node.Product;

public class SearchedProductsAdapter extends ListAdapter<Product, SearchedProductsAdapter.ViewHolder>  {

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {

        @Override
        public boolean areItemsTheSame(@NonNull Product model, @NonNull Product t1) {
            return model.areItemsTheSame(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product model, @NonNull Product t1) {
            return model.areContentsTheSame(t1);
        }

    };

    SearchedProductsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_views_cell, parent, false);
        return new SearchedProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = getItem(position);
        if (product != null) holder.bind(product);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgHolder;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHolder = itemView.findViewById(R.id.img_holder);
        }

        private void bind(@NonNull Product product) {
            String url = product.getGalleryUrls().get(0);
            Glide.with(imgHolder.getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imgHolder);
        }

    }

}
