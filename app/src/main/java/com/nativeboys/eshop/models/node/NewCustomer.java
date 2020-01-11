package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class NewCustomer {

    String first_name, last_name;
    String email, profile_image_url, phone_number;

    NewCustomer(String first_name, String last_name, String email, String profile_image_url, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.profile_image_url = profile_image_url;
        this.phone_number = phone_number;
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

    public String getProfileImageUrl() {
        return profile_image_url;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewCustomer{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }
}
