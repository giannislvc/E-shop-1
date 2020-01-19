package com.nativeboys.eshop.customViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AutoCompleteAdapter<T> extends ArrayAdapter<T> implements AdapterView.OnItemSelectedListener {

    public interface OnItemSelectedListener<T> {
        void onItemSelected(@Nullable T model);
    }

    private final int resource;
    private OnItemSelectedListener<T> onItemSelectedListener;

    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, 0);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        onBindView(getItem(position), convertView);
        return convertView;
    }

    public abstract void onBindView(@Nullable T model, @NonNull View viewHolder);

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(getItem(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        onItemSelectedListener.onItemSelected(null);
    }

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

    public void setOnItemSelectedListener(@NonNull AutoCompleteTextView tv, OnItemSelectedListener<T> onItemSelectedListener) {
        tv.setOnItemSelectedListener(this);
        this.onItemSelectedListener = onItemSelectedListener;
    }

}