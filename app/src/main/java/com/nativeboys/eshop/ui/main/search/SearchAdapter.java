package com.nativeboys.eshop.ui.main.search;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.AutoCompleteAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends AutoCompleteAdapter<String> {

    SearchAdapter(@NonNull Context context) {
        super(context, R.layout.text_search_cell);
    }

    @Override
    public void onBindView(@Nullable String model, @NonNull View viewHolder) {
        TextView tv = viewHolder.findViewById(R.id.text_view);
        tv.setText(model);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> suggestions = new ArrayList<>(getDataSet());
                results.values = suggestions;
                results.count = suggestions.size();
                return  results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) { }
        };
    }

}
