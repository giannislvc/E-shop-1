package com.nativeboys.eshop.tools;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ConversationViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String friendId;

    public ConversationViewModelFactory(@NonNull Application application, @NonNull String friendId) {
        this.application = application;
        this.friendId = friendId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConversationViewModel(application, friendId);
    }
}
