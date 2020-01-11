package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class Category {

    private String category_id, name;
    private String description, image_url;

    public Category(String category_id, String name, String description, String image_url) {
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
    }

    public String getCategoryId() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return image_url;
    }

    @NonNull
    @Override
    public String toString() {
        return "Category{" +
                "category_id='" + category_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
