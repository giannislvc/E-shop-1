package com.nativeboys.eshop.callbacks;

import androidx.annotation.Nullable;

public interface Completion<T> {
    void onResponse(@Nullable T model);
}
