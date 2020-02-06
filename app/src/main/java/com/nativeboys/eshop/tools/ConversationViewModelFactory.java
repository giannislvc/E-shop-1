package com.nativeboys.eshop.tools;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ConversationViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String userId, friendId;

    public ConversationViewModelFactory(@NonNull Application application, @NonNull String userId, @NonNull String friendId) {
        this.application = application;
        this.userId = userId;
        this.friendId = friendId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConversationViewModel(application, userId, friendId);
    }
}
