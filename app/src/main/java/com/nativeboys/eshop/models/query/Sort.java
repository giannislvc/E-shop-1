package com.nativeboys.eshop.models.query;

import androidx.annotation.NonNull;

public class Sort {

    private int order_by, limit, start;
    private boolean asc;

    public Sort(int order_by, int limit, int start, boolean asc) {
        this.order_by = order_by;
        this.limit = limit;
        this.start = start;
        this.asc = asc;
    }

    public int getOrderBy() {
        return order_by;
    }

    public int getLimit() {
        return limit;
    }

    public int getStart() {
        return start;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setOrder_by(int order_by) {
        this.order_by = order_by;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    @NonNull
    @Override
    public String toString() {
        return "Sort{" +
                "order_by=" + order_by +
                ", limit=" + limit +
                ", start=" + start +
                ", asc=" + asc +
                '}';
    }
}
