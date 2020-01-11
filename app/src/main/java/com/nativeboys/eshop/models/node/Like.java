package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class Like {

    private String customer_id, product_id;

    public Like(String customer_id, String product_id) {
        this.customer_id = customer_id;
        this.product_id = product_id;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public String getProductId() {
        return product_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Like{" +
                "customer_id='" + customer_id + '\'' +
                ", product_id='" + product_id + '\'' +
                '}';
    }
}
