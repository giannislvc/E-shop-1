package com.nativeboys.eshop.ui.main.products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.node.Product;

public class ProductsAdapter extends PagedListAdapter<Product, ProductsAdapter.ProductsViewHolder> {

    public interface OnProductClickListener {
        void onClick(View itemView, Product product);
    }

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

    private OnProductClickListener clickListener;

    ProductsAdapter() {
        super(DIFF_CALLBACK);
    }

    void setOnProductClickListener(OnProductClickListener listener) {
        clickListener = listener;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cell, parent, false);
        return new ProductsAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Product product = getItem(position);
        if (product != null) holder.bind(product);
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageHolder, likeImg;
        private TextView nameField, priceField, likesField, viewsField;

        ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHolder = itemView.findViewById(R.id.image_holder);
            nameField = itemView.findViewById(R.id.name_field);
            priceField = itemView.findViewById(R.id.price_field);
            likesField = itemView.findViewById(R.id.likes_field);
            likeImg = itemView.findViewById(R.id.like_img);
            viewsField = itemView.findViewById(R.id.views_field);
            itemView.setOnClickListener(v -> {
                if (clickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                clickListener.onClick(v, getItem(position));
            });
        }

        private void bind(@NonNull Product product) {
            nameField.setText(product.getName());

            String likesQuantity = product.getLikesQty();
            String viewsQuantity = product.getViewsQty();

            boolean zeroLikes = likesQuantity.equals("0");
            boolean zeroViews = viewsQuantity.equals("0");

            if (!zeroLikes) {
                String likes = String.format(likesField.getResources().getString(R.string.likes), likesQuantity);
                likesField.setText(likes);
                likesField.setVisibility(View.VISIBLE);
            } else {
                likesField.setVisibility(View.GONE);
            }

            if (!zeroViews) {
                int resource = zeroLikes ? R.string.views : R.string._views;
                String views = String.format(viewsField.getResources().getString(resource), viewsQuantity);
                viewsField.setText(views);
                viewsField.setVisibility(View.VISIBLE);
            } else {
                viewsField.setVisibility(View.GONE);
            }

            likeImg.setVisibility(product.isLiked() ? View.VISIBLE : View.INVISIBLE);

            String price = String.format(priceField.getResources().getString(R.string.price), product.getPrice());
            priceField.setText(price);
            Glide.with(imageHolder.getContext())
                    .load(product.getGalleryUrls().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imageHolder);
        }

    }

}
