package com.nativeboys.eshop.ui.main.settings;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.SettingsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    interface OnDetailClickLister<T extends SettingsModel> {
        void onDetailClicked(T model);
    }

    private OnDetailClickLister lister;
    private List<? extends SettingsModel> dataSet;

    DetailsAdapter() {
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    public void setLister(OnDetailClickLister lister) {
        this.lister = lister;
    }

    <T extends SettingsModel> void setDataSet(@NonNull List<T> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            LinearLayout container = itemView.findViewById(R.id.container);
            textView = itemView.findViewById(R.id.text_view);
            container.setOnClickListener(view -> {
                if (lister == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                lister.onDetailClicked(dataSet.get(position));
            });
        }

        private <T extends SettingsModel> void bind(T model) {
            if (model.isSelected()) {
                Glide.with(textView.getContext()).load(R.drawable.ic_check_black_24dp).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            textView.setText(model.getDescription());
        }

    }

}
