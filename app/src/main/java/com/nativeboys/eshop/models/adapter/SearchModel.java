package com.nativeboys.eshop.models.adapter;

import androidx.annotation.NonNull;

import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;

public class SearchModel {

    private Filter filter;
    private Sort sort;

    public SearchModel(Filter filter, Sort sort) {
        this.filter = filter;
        this.sort = sort;
    }

    public static SearchModel Create(String categoryId, int order) {
        Filter filter = new Filter(categoryId, null);
        Sort sort = new Sort(10, 0, order, false);
        return new SearchModel(filter, sort);
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @NonNull
    @Override
    public String toString() {
        return "SearchModel{" +
                "filter=" + filter +
                ", sort=" + sort +
                '}';
    }
}
