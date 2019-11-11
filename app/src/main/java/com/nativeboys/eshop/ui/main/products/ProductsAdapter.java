package com.nativeboys.eshop.ui.main.products;

import android.content.Context;
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
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.UserModel;

public class ProductsAdapter extends ListAdapter<UserModel, ProductsAdapter.ProductsViewHolder> {

    final private Context context;
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

    ProductsAdapter(@NonNull Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
        String text = String.format(context.getResources().getString(R.string.user_name_format), model.getName(), model.getLastName());
        holder.user_name.setText(text);
        Glide.with(context).load(model.getPickPath()).into(holder.user_image);
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {

        private ImageView user_image;
        private TextView user_name;

        ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            itemView.setOnClickListener(v -> {
                if (clickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                clickListener.onClick(v, getItem(position));
            });
        }
    }

}
