package com.nativeboys.eshop.models.node;

import com.nativeboys.eshop.models.SettingsModel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category implements SettingsModel {

    private final String category_id, name;
    private final String description, image_url;

    private boolean selected;

    public Category(String category_id, String name, String description, String image_url) {
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.selected = false;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return image_url;
    }

    public String getCategoryId() {
        return category_id;
    }

    public boolean areItemsTheSame(@NonNull Category other) {
        return category_id.equals(other.category_id);
    }

    public boolean areContentsTheSame(@NonNull Category other) {
        return category_id.equals(other.category_id) &&
                name.equals(other.name) &&
                Objects.equals(description, other.description) &&
                Objects.equals(image_url, other.image_url) &&
                selected == other.selected;
    }

    private Category getClone() {
        Category category = new Category(category_id, name, description, image_url);
        category.setSelected(selected);
        return category;
    }

    public static List<Category> getClones(@NonNull List<Category> list) {
        List<Category> clones = new ArrayList<>();
        for (Category category : list) {
            clones.add(category.getClone());
        }
        return clones;
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
        return Objects.hash(category_id, name, description, image_url, selected);
    }

    @NonNull
    @Override
    public String toString() {
        return "Category{" +
                "category_id='" + category_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", selected=" + selected +
                '}';
    }
}
