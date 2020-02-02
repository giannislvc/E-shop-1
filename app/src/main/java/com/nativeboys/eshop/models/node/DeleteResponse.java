package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

public class DeleteResponse {

    private final String message;

    public DeleteResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeleteResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
