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

    private DatabaseReference conversationsRef, metadataRef;

    private MutableLiveData<List<MessageModel>> messages = new MutableLiveData<>();

    private final String userId, friendId, conversationId;

    private final Query query;

    private final ChildEventListener childListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String id = dataSnapshot.getKey();
            if (id == null) return;
            MessageModel message = dataSnapshot.getValue(MessageModel.class);
            if (message != null) {
                message.setId(id);
                List<MessageModel> messages = ConversationViewModel.this.messages.getValue();
                if (messages == null) messages = new ArrayList<>();
                messages.add(message);
                ConversationViewModel.this.messages.setValue(messages);
            }
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

    private final ValueEventListener listener = new ValueEventListener() {
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
            ConversationViewModel.this.messages.setValue(messages);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    ConversationViewModel(@NonNull Application application, @NonNull String conversationId, @NonNull String userId, @NonNull String friendId) {
        super(application);
        this.userId = userId;
        this.friendId = friendId;
        this.conversationId = conversationId;
        conversationsRef = FirebaseDatabase.getInstance().getReference(CONVERSATIONS);
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);
        query = conversationsRef.child(conversationId).limitToLast(5);
        query.addChildEventListener(childListener);
        //conversationsRef.child(conversationId).orderByKey().endAt("-LrGBjdwsCmUjOVD90za").limitToLast(5).addChildEventListener(childListener);
    }

    LiveData<List<MessageModel>> getMessages() {
        return messages;
    }

    void sendMessage(String text) {
        String messageId = conversationsRef.push().getKey();
        if (messageId != null) {
            MessageModel message = new MessageModel(userId, text, new Timestamp(System.currentTimeMillis()).getTime(), 1);
            // TODO: Replace with batch operations (HashMap)
            conversationsRef.child(conversationId).child(messageId).setValue(message);
            metadataRef.child(userId).child(friendId).child("lastMessage").setValue(message);
            metadataRef.child(friendId).child(userId).child("lastMessage").setValue(message);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (query != null) query.removeEventListener(childListener);
    }
}
