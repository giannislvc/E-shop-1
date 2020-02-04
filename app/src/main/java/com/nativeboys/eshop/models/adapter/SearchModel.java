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

    public SearchModel getClone() {
        return new SearchModel(filter.getClone(), sort.getClone());
    }

    public SearchModel init(String categoryId, int order) {
        SearchModel clone = getClone();
        clone.getFilter().setCategoryId(categoryId);
        clone.getSort().setOrderBy(order);
        clone.getSort().setStart(0);
        return clone;
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
