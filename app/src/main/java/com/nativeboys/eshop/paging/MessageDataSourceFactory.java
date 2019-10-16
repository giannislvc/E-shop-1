package com.nativeboys.eshop.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.google.firebase.database.DatabaseReference;
import com.nativeboys.eshop.models.MessageModel;

public class MessageDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<String, MessageModel>> messageLiveDataSource = new MutableLiveData<>();

    private final DatabaseReference database;

    public MessageDataSourceFactory(DatabaseReference database) {
        this.database = database;
    }

    @NonNull
    @Override
    public DataSource create() {
        MessageDataSource messageDataSource = new MessageDataSource(database);
        messageLiveDataSource.postValue(messageDataSource);
        return messageDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<String, MessageModel>> getMessageLiveDataSource() {
        return messageLiveDataSource;
    }
}

// database = FirebaseDatabase.getInstance().getReference(CONVERSATIONS).child(conversationId);
