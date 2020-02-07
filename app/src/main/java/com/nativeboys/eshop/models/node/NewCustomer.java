package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class NewCustomer {

    final String first_name, last_name;
    final String email, customer_id;

    public NewCustomer(String first_name, String last_name, String email, String customer_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.customer_id = customer_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getCustomerId() {
        return customer_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewCustomer{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", customer_id='" + customer_id + '\'' +
                '}';
    }
}
