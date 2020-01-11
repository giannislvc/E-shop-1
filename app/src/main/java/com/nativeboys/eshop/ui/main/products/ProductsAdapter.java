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
            return model.getProductId().equals(t1.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product model, @NonNull Product t1) {
            return model.equals(t1);
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

        private ImageView productImage;
        private TextView productNameField, productPriceField ,viewsField, likesField;

        ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_holder);
            productNameField = itemView.findViewById(R.id.product_name_field);
            productPriceField = itemView.findViewById(R.id.product_price_field);
            //viewsField = itemView.findViewById(R.id.views_field);
            //likesField = itemView.findViewById(R.id.likes_field);
            //productName = itemView.findViewById(R.id.product_name);

            itemView.setOnClickListener(v -> {
                if (clickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                clickListener.onClick(v, getItem(position));
            });
        }

        private void bind(@NonNull Product product) {
            // String text = String.format(user_name.getContext().getResources().getString(R.string.user_name_format), model.getName(), model.getLastName());
            //productName.setText(product.getName());
            //likesField.setText(product.getLikesQty());
            //viewsField.setText(product.getViewsQty());
            productNameField.setText(product.getName());
            String price = String.format(productPriceField.getResources().getString(R.string.price), product.getPrice());
            productPriceField.setText(price);
            Glide.with(productImage.getContext())
                    .load(product.getGalleryUrls().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(productImage);
        }

    }

}
