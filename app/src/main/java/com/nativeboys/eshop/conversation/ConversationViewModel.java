package com.nativeboys.eshop.conversation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nativeboys.eshop.models.MessageModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

class ConversationViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();

    private final static String CONVERSATIONS = "conversations";
    private final static String METADATA = "metadata";
    private final static int MESSAGES_LIMIT = 10;

    private DatabaseReference conversationsRef, metadataRef;

    private MutableLiveData<List<MessageModel>> messages;

    private final String userId, friendId;

    private final Query query;

    private boolean fetchingDataState;

    {
        messages = new MutableLiveData<>();
        fetchingDataState = false;
    }

    private final ChildEventListener childListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            MessageModel message = dataSnapshot.getValue(MessageModel.class);
            String id = dataSnapshot.getKey();
            if (id == null || message == null) return;
            message.setId(id);
            List<MessageModel> messages = ConversationViewModel.this.messages.getValue();
            if (messages == null) messages = new ArrayList<>();
            messages.add(message);
            ConversationViewModel.this.messages.setValue(messages);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // TODO : Implement internet error
        }
    };

    ConversationViewModel(@NonNull Application application, @NonNull String conversationId, @NonNull String userId, @NonNull String friendId) {
        super(application);
        this.userId = userId;
        this.friendId = friendId;
        conversationsRef = FirebaseDatabase.getInstance().getReference(CONVERSATIONS).child(conversationId);
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);

        conversationsRef.keepSynced(false);
        query = conversationsRef.limitToLast(MESSAGES_LIMIT);
        query.addChildEventListener(childListener);
    }

    LiveData<List<MessageModel>> getMessages() {
        return messages;
    }

    void sendMessage(String text) {
        String messageId = conversationsRef.push().getKey();
        if (messageId != null) {
            MessageModel message = new MessageModel(userId, text, new Timestamp(System.currentTimeMillis()).getTime(), 1);
            // TODO: Replace with batch operations (HashMap)
            conversationsRef.child(messageId).setValue(message);
            metadataRef.child(userId).child(friendId).child("lastMessage").setValue(message);
            metadataRef.child(friendId).child(userId).child("lastMessage").setValue(message);
        }
    }

    void getPreviousData() {
        if(fetchingDataState) return;
        fetchingDataState = true;
        List<MessageModel> list = messages.getValue();
        if (list == null || list.isEmpty()) return;
        String messageId = list.get(0).getId();
        if (messageId == null) return;
        conversationsRef.orderByKey().limitToLast(MESSAGES_LIMIT).endAt(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MessageModel> messages = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String id = userSnapshot.getKey();
                    if (id == null || id.equals(messageId)) continue;
                    MessageModel message = userSnapshot.getValue(MessageModel.class);
                    if (message != null) {
                        message.setId(id);
                        messages.add(message);
                    }
                }
                list.addAll(0, messages);
                ConversationViewModel.this.messages.setValue(list);
                fetchingDataState = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fetchingDataState = false;
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (query != null) query.removeEventListener(childListener);
    }
}
