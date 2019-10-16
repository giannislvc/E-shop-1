package com.nativeboys.eshop.paging;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nativeboys.eshop.models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MessageDataSource extends PageKeyedDataSource<String, MessageModel> {

    private final static int PAGE_SIZE = 10;

    private final DatabaseReference database;

    MessageDataSource(DatabaseReference database) {
        this.database = database;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, MessageModel> callback) {

        database.limitToLast(PAGE_SIZE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MessageModel> messages = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String id = userSnapshot.getKey();
                    if (id == null) continue;
                    MessageModel message = userSnapshot.getValue(MessageModel.class);
                    if (message != null) {
                        message.setId(id);
                        messages.add(message);
                    }
                }
                callback.onResult(messages, messages.get(0).getId(), null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, MessageModel> callback) {

        database.orderByKey().endAt(params.key).limitToLast(PAGE_SIZE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MessageModel> messages = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String id = userSnapshot.getKey();
                    if (id == null) continue;
                    MessageModel message = userSnapshot.getValue(MessageModel.class);
                    if (message != null) {
                        message.setId(id);
                        messages.add(message);
                    }
                }
                callback.onResult(messages, messages.get(0).getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, MessageModel> callback) { }
}
