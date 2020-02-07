package com.nativeboys.eshop.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String productId;

    public ProductViewModelFactory(@NonNull Application application, @Nullable String productId) {
        this.application = application;
        this.productId = productId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProductViewModel(application, productId);
    }
}
