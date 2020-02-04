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

public abstract class AutoCompleteAdapter<T> extends ArrayAdapter<T> implements AdapterView.OnItemClickListener {

    public interface OnItemClickListener<T> {
        void onItemClick(@Nullable T model);
    }

    private final int resource;
    private OnItemClickListener<T> onItemClickListener;

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getItem(position));
        }
    }

    public void setOnItemClickListener(@NonNull AutoCompleteTextView tv, @NonNull OnItemClickListener<T> onItemClickListener) {
        tv.setOnItemClickListener(this);
        this.onItemClickListener = onItemClickListener;
    }

}