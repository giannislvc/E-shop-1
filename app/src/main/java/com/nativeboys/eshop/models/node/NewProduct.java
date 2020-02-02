package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

import java.util.List;

public class NewProduct {

    private final String name, price, description, details, uploader_id, category_id;
    private final List<String> hash_tags;

    public NewProduct(String name, String price, String description, String details, List<String> hash_tags, String uploader_id, String category_id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.details = details;
        this.hash_tags = hash_tags;
        this.uploader_id = uploader_id;
        this.category_id = category_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewProduct{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", details='" + details + '\'' +
                ", hash_tags='" + hash_tags + '\'' +
                ", uploader_id='" + uploader_id + '\'' +
                ", category_id='" + category_id + '\'' +
                '}';
    }
}
