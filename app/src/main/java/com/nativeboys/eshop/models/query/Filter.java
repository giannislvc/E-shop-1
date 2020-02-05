package com.nativeboys.eshop.models.query;

import androidx.annotation.NonNull;

public class Filter {

    private String category_id;
    private String text;

    public Filter(String categoryId, String text) {
        this.category_id = categoryId;
        this.text = text;
    }

    public Filter() {}

    public String getCategoryId() {
        return category_id;
    }

    public String getText() {
        return text;
    }

    public void setCategoryId(String categoryId) {
        this.category_id = categoryId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Filter getClone() {
        return new Filter(category_id, text);
    }

    @NonNull
    @Override
    public String toString() {
        return "Filter{" +
                "categoryId='" + category_id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
