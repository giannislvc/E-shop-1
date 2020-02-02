package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class LikeResponse {

    private final String user_id, product_id, like_timestamp;
    private final boolean liked;

    public LikeResponse(String customer_id, String product_id, String user_id, String product_id1, String like_timestamp, boolean liked) {
        this.user_id = user_id;
        this.product_id = product_id1;
        this.like_timestamp = like_timestamp;
        this.liked = liked;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getLike_timestamp() {
        return like_timestamp;
    }

    public boolean isLiked() {
        return liked;
    }

    @NonNull
    @Override
    public String toString() {
        return "LikeResponse{" +
                "user_id='" + user_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", like_timestamp='" + like_timestamp + '\'' +
                ", liked=" + liked +
                '}';
    }
}
