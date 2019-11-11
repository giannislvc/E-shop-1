package com.nativeboys.eshop;

import androidx.annotation.Nullable;

public interface AuthCompleteListener {
    void onComplete(boolean success, @Nullable String message);
}
