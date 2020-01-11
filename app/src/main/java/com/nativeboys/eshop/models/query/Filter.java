package com.nativeboys.eshop.models.query;

import androidx.annotation.NonNull;

public class Filter {

    private String categoryId;
    private String text;

    public Filter(String categoryId, String text) {
        this.categoryId = categoryId;
        this.text = text;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getText() {
        return text;
    }

    @NonNull
    @Override
    public String toString() {
        return "Filter{" +
                "categoryId='" + categoryId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
