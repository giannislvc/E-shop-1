package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class DetailedCustomer extends Customer {

    private final String likes, products;

    public DetailedCustomer(String first_name, String last_name, String email, String customer_id, String date_joined, String profile_image_url, String phone_number, String likes, String products) {
        super(first_name, last_name, email, customer_id, date_joined, profile_image_url, phone_number);
        this.likes = likes;
        this.products = products;
    }

    public String getLikes() {
        return likes;
    }

    public String getProducts() {
        return products;
    }

    public String getFullName() {
        return first_name +
                " " +
                last_name;
    }

    @NonNull
    @Override
    public String toString() {
        return "DetailedCustomer{" +
                "likes='" + likes + '\'' +
                ", products='" + products + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", customer_id='" + customer_id + '\'' +
                '}';
    }
}
