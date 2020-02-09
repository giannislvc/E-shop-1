package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

import java.util.List;

public class NewProduct {

    private final String name, price, description, details, category_id, uploader_id;
    private final List<String> hash_tags, gallery_urls;

    // Create Product
    public NewProduct(String name, String price, String description, String details, List<String> hash_tags, String uploader_id, String category_id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.details = details;
        this.hash_tags = hash_tags;
        this.uploader_id = uploader_id;
        this.category_id = category_id;
        this.gallery_urls = null;
    }

    // Update Product
    public NewProduct(String name, String price, String description, String details, String category_id, List<String> hash_tags, List<String> gallery_urls) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.details = details;
        this.category_id = category_id;
        this.hash_tags = hash_tags;
        this.gallery_urls = gallery_urls;
        this.uploader_id = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewProduct{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", details='" + details + '\'' +
                ", category_id='" + category_id + '\'' +
                ", uploader_id='" + uploader_id + '\'' +
                ", hash_tags=" + hash_tags +
                ", gallery_urls=" + gallery_urls +
                '}';
    }
}
