package com.nativeboys.eshop.models.query;

import androidx.annotation.NonNull;

public class Filter {

    private String categoryId;
    private String text;

    public Filter(String categoryId, String text) {
        this.categoryId = categoryId;
        this.text = text;
    }

    public Filter() {}

    public String getCategoryId() {
        return categoryId;
    }

    public String getText() {
        return text;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Filter getClone() {
        return new Filter(categoryId, text);
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
