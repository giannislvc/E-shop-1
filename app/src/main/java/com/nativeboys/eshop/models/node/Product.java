package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Product {

    String product_id, name, price, upload_time;
    List<String> gallery_urls;
    String uploader_id, category_id, views_qty, likes_qty;
    boolean liked;

    Product(String product_id, String name, String price, String upload_time, List<String> gallery_urls, String uploader_id, String category_id, String views_qty, String likes_qty, boolean liked) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.upload_time = upload_time;
        this.gallery_urls = gallery_urls;
        this.uploader_id = uploader_id;
        this.category_id = category_id;
        this.views_qty = views_qty;
        this.likes_qty = likes_qty;
        this.liked = liked;
    }

    public String getProductId() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getUploadTime() {
        return upload_time;
    }

    public List<String> getGalleryUrls() {
        return gallery_urls;
    }

    public String getUploaderId() {
        return uploader_id;
    }

    public String getCategoryId() {
        return category_id;
    }

    public String getViewsQty() {
        return views_qty;
    }

    public String getLikesQty() {
        return likes_qty;
    }

    public boolean isLiked() {
        return liked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return liked == product.liked &&
                product_id.equals(product.product_id) &&
                name.equals(product.name) &&
                price.equals(product.price) &&
                upload_time.equals(product.upload_time) &&
                gallery_urls.equals(product.gallery_urls) &&
                uploader_id.equals(product.uploader_id) &&
                category_id.equals(product.category_id) &&
                views_qty.equals(product.views_qty) &&
                likes_qty.equals(product.likes_qty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id, name, price, upload_time, gallery_urls, uploader_id, category_id, views_qty, likes_qty, liked);
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "product_id='" + product_id + '\'' +
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
