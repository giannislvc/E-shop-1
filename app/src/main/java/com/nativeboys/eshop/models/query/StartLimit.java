package com.nativeboys.eshop.models.query;

import androidx.annotation.NonNull;

public class StartLimit {

    int limit, start;

    public StartLimit(int limit, int start) {
        this.limit = limit;
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @NonNull
    @Override
    public String toString() {
        return "StartLimit{" +
                "limit=" + limit +
                ", start=" + start +
                '}';
    }
}
