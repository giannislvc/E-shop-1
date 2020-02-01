package com.nativeboys.eshop.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.node.Category;

public class CategoriesAdapter extends ListAdapter<Category, CategoriesAdapter.ViewHolder> {

    private final static DiffUtil.ItemCallback<Category> callback = new DiffUtil.ItemCallback<Category>() {

        @Override
        public boolean areItemsTheSame(@NonNull Category model, @NonNull Category t1) {
            return model.areItemsTheSame(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category model, @NonNull Category t1) {
            return model.areContentsTheSame(t1);
        }

    };

    public CategoriesAdapter() {
        super(callback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category model = getItem(position);
        if (model != null) holder.bind(model);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageHolder;
        private TextView label;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHolder = itemView.findViewById(R.id.image_holder);
            label = itemView.findViewById(R.id.label);
        }

        private void bind(@NonNull Category category) {
            Glide.with(imageHolder.getContext())
                    .load(category.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageHolder);
            label.setText(category.getName());
        }

    }

}
