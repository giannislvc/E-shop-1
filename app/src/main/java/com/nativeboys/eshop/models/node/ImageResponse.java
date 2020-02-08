package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class ImageResponse {

    private final String image;

    public ImageResponse(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageResponse{" +
                "image='" + image + '\'' +
                '}';
    }
}
