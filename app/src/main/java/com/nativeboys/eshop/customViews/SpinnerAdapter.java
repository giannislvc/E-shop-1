package com.nativeboys.eshop.customViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SpinnerAdapter<T> extends ArrayAdapter<T> implements AdapterView.OnItemSelectedListener {

    public interface OnItemSelectedListener<T> {
        void onItemSelected(@Nullable T model);
    }

    private final int parentLayout, childLayout;
    private OnItemSelectedListener<T> onItemSelectedListener;

    public SpinnerAdapter(@NonNull Context context, int parentLayout, int childLayout) {
        super(context, 0);
        this.parentLayout = parentLayout;
        this.childLayout = childLayout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(parentLayout, parent, false);
        onBindParent(getItem(position), convertView);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(childLayout, parent, false);
        onBindChild(getItem(position), convertView);
        return convertView;
    }

    public abstract void onBindParent(@Nullable T model, @NonNull View viewHolder);

    public abstract void onBindChild(@Nullable T model, @NonNull View viewHolder);

    public List<T> getDataSet() {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < getCount(); i ++) {
            T item = getItem(i);
            if (item != null) list.add(item);
        }
        return list;
    }

    public void setDataSet(@NonNull List<T> list) {
        List<T> dataSet = getDataSet();
        if (dataSet.equals(list)) return;
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(getItem(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    public void setOnItemClickListener(@NonNull Spinner tv, @NonNull OnItemSelectedListener<T> onItemSelectedListener) {
        tv.setOnItemSelectedListener(this);
        this.onItemSelectedListener = onItemSelectedListener;
    }

}
