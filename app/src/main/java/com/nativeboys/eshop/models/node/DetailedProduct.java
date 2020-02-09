package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

import java.util.List;

public class DetailedProduct extends Product {

    private String description, details;
    private List<String> hash_tags;

    public DetailedProduct(String product_id, String name, String description, String details, List<String> hash_tags, String price, String upload_time, List<String> gallery_urls, String uploader_id, String category_id, String views_qty, String likes_qty, boolean liked) {
        super(product_id, name, price, upload_time, gallery_urls, uploader_id, category_id, views_qty, likes_qty, liked);
        this.description = description;
        this.details = details;
        this.hash_tags = hash_tags;
    }

    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    public List<String> getHashTags() {
        return hash_tags;
    }

    public String getHashTagsAsText() {
        StringBuilder builder = new StringBuilder();
        if (hash_tags != null) {
            for (String tag : hash_tags) {
                builder.append(tag).append(",");
            }
        }
        String text = builder.toString();
        if (text.length() > 0) text = text.substring(0, text.length() - 1);
        return text;
    }

    @NonNull
    @Override
    public String toString() {
        return "DetailedProduct{" +
                "description='" + description + '\'' +
                ", details='" + details + '\'' +
                ", hash_tags='" + hash_tags + '\'' +
                ", product_id='" + product_id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", upload_time='" + upload_time + '\'' +
                ", gallery_urls='" + gallery_urls + '\'' +
                ", uploader_id='" + uploader_id + '\'' +
                ", category_id='" + category_id + '\'' +
                ", views_qty='" + views_qty + '\'' +
                ", likes_qty='" + likes_qty + '\'' +
                ", liked=" + liked +
                '}';
    }
}
