package com.nativeboys.eshop.viewModels.textSearch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TextSearchViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String text;

    public TextSearchViewModelFactory(Application application, String text) {
        this.application = application;
        this.text = text;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TextSearchViewModel(application, text);
    }
}
