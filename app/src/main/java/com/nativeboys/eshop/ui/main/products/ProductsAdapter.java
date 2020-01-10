package com.nativeboys.eshop.ui.main.products;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.UserModel;

public class ProductsAdapter extends ListAdapter<UserModel, ProductsAdapter.ProductsViewHolder> {

    private final static String MOCK_IMAGE = "https://external.webstorage.gr/Product-Images/1190049/ps4slim-1000-1190049.jpg";

    private OnUserClickListener clickListener;

    public interface OnUserClickListener {
        void onClick(View itemView, UserModel user);
    }

    void setOnUserClickListener(OnUserClickListener listener) {
        clickListener = listener;
    }

    private static final DiffUtil.ItemCallback<UserModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<UserModel>() {

        @Override
        public boolean areItemsTheSame(@NonNull UserModel model, @NonNull UserModel t1) {
            return model.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserModel model, @NonNull UserModel t1) {
            return model.getName().equals(t1.getName()) && model.getLastName().equals(t1.getLastName()) && model.getPickPath().equals(t1.getPickPath());
        }

    };

    ProductsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cell, parent, false);
        return new ProductsAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        UserModel model = getItem(position);
        if (model != null) holder.bind(model);
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageHolder;
        private TextView nameField, descriptionField;

        ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHolder = itemView.findViewById(R.id.product_image_holder);
            //nameField = itemView.findViewById(R.id.product_name_field);
            //descriptionField = itemView.findViewById(R.id.product_description_field);
            itemView.setOnClickListener(v -> {
                if (clickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                clickListener.onClick(v, getItem(position));
            });
        }

        private void bind(@NonNull UserModel model) {
            Glide.with(imageHolder.getContext())
                    .load(MOCK_IMAGE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imageHolder);
        }

    }

}
