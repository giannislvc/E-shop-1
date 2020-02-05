package com.nativeboys.eshop.ui.main.settings;

import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.models.SettingsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    interface OnDetailClickLister<T extends SettingsModel> {
        void onDetailCliked(T model);
    }

    private OnDetailClickLister lister;
    private List<? extends SettingsModel> dataSet;

    DetailsAdapter() {
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    public void setLister(OnDetailClickLister lister) {
        this.lister = lister;
    }

    public <T extends SettingsModel> void setDataSet(@NonNull List<T> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private <T extends SettingsModel> void bind(T model) {

        }

    }

}
