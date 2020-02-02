package com.nativeboys.eshop.ui.main.product;

import android.net.Uri;
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

public class ProductImageAdapter extends ListAdapter<ImageSliderModel, ProductImageAdapter.ViewHolder> {

    private final static DiffUtil.ItemCallback<ImageSliderModel> callback = new DiffUtil.ItemCallback<ImageSliderModel>() {

        @Override
        public boolean areItemsTheSame(@NonNull ImageSliderModel model, @NonNull ImageSliderModel t1) {
            return model.equals(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImageSliderModel model, @NonNull ImageSliderModel t1) {
            return model.equals(t1);
        }

    };

    interface OnRemoveImageClickListener {
        void onRemoveClicked(int position);
    }

    private OnRemoveImageClickListener onRemoveListener;
    private boolean isUserProduct;

    ProductImageAdapter() {
        super(callback);
        isUserProduct = false;
    }

    void setIsClientProduct(boolean isUserProduct) {
        this.isUserProduct = isUserProduct;
        notifyDataSetChanged();
    }

    void setOnRemoveListener(OnRemoveImageClickListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageSliderModel model = getItem(position);
        if (model != null) holder.bind(model);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageHolder, deleteBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHolder = itemView.findViewById(R.id.image_holder);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            deleteBtn.setOnClickListener(v -> {
                if (onRemoveListener == null) return;
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onRemoveListener.onRemoveClicked(position);
                }
            });
        }

        private void bind(@NonNull ImageSliderModel model) {
            String url = model.getUrl();
            Uri uri = model.getUri();
            if (url == null && uri == null) return;
            Glide.with(imageHolder.getContext())
                    .load(url != null ? url : uri)
                    .transform(new CenterCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageHolder);
            deleteBtn.setVisibility(isUserProduct ? View.VISIBLE : View.GONE);
        }

    }

}
