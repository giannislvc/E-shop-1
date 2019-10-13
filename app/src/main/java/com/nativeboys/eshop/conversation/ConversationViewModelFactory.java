package com.nativeboys.eshop.conversation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ConversationViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String conversationId;
    private String userId, friendId;

    public ConversationViewModelFactory(@NonNull Application application, @NonNull String conversationId, @NonNull String userId, @NonNull String friendId) {
        this.application = application;
        this.conversationId = conversationId;
        this.userId = userId;
        this.friendId = friendId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConversationViewModel(application, conversationId, userId, friendId);
    }
}
