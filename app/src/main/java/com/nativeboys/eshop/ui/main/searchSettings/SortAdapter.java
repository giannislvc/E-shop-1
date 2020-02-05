package com.nativeboys.eshop.ui.main.searchSettings;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.SpinnerAdapter;
import com.nativeboys.eshop.models.adapter.SortModel;

public class SortAdapter extends SpinnerAdapter<SortModel> {

    SortAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_parent_cell, R.layout.spinner_child_cell);
    }

    @Override
    public void onBindParent(@Nullable SortModel model, @NonNull View viewHolder) {
        setUpView(viewHolder, model);
    }

    @Override
    public void onBindChild(@Nullable SortModel model, @NonNull View viewHolder) {
        setUpView(viewHolder, model);
    }

    private void setUpView(@NonNull View view, @Nullable SortModel model) {
        TextView textView = view.findViewById(R.id.text_view);
        if (model != null) textView.setText(model.getDescription());
    }

}
