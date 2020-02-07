package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class Customer extends NewCustomer {

    private final String customer_id, date_joined;

    public Customer(String customer_id, String first_name, String last_name, String email, String profile_image_url, String phone_number, String date_joined) {
        super(first_name, last_name, email, profile_image_url, phone_number);
        this.customer_id = customer_id;
        this.date_joined = date_joined;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public String getDateJoined() {
        return date_joined;
    }

    @NonNull
    @Override
    public String toString() {
        return "Customer{" +
                "customer_id='" + customer_id + '\'' +
                ", date_joined='" + date_joined + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }
}
