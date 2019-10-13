package com.nativeboys.eshop;

import androidx.annotation.Nullable;

public interface Completion<T> {
    void onResponse(@Nullable T model);
}
