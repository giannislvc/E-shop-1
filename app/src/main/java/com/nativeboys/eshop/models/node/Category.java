package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

import java.util.Objects;

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

    public boolean areItemsTheSame(@NonNull Category other) {
        return category_id.equals(other.category_id);
    }

    public boolean areContentsTheSame(@NonNull Category other) {
        return category_id.equals(other.category_id) &&
                name.equals(other.name) &&
                Objects.equals(description, other.description) &&
                Objects.equals(image_url, other.image_url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return areItemsTheSame(category) && areContentsTheSame(category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category_id, name, description, image_url);
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
