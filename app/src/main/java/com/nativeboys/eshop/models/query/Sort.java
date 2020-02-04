package com.nativeboys.eshop.models.query;

import androidx.annotation.NonNull;

public class Sort extends StartLimit {

    private int order_by;
    private boolean asc;

    public Sort(int limit, int start, int order_by, boolean asc) {
        super(limit, start);
        this.order_by = order_by;
        this.asc = asc;
    }

    public int getOrderBy() {
        return order_by;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setOrderBy(int order_by) {
        this.order_by = order_by;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public Sort getClone() {
        return new Sort(limit, start, order_by, asc);
    }

    @NonNull
    @Override
    public String toString() {
        return "Sort{" +
                "order_by=" + order_by +
                ", asc=" + asc +
                ", limit=" + limit +
                ", start=" + start +
                '}';
    }
}
