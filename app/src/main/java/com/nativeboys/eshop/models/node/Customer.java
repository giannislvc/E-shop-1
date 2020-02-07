package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class Customer extends NewCustomer {

    private final String date_joined;
    private final String profile_image_url, phone_number;

    public Customer(String first_name, String last_name, String email, String customer_id, String date_joined, String profile_image_url, String phone_number) {
        super(first_name, last_name, email, customer_id);
        this.date_joined = date_joined;
        this.profile_image_url = profile_image_url;
        this.phone_number = phone_number;
    }

    public String getDateJoined() {
        return date_joined;
    }

    public String getProfileImageUrl() {
        return profile_image_url;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    @NonNull
    @Override
    public String toString() {
        return "Customer{" +
                "date_joined='" + date_joined + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", customer_id='" + customer_id + '\'' +
                '}';
    }
}
