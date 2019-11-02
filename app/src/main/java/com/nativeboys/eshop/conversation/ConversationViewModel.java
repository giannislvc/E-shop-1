package com.nativeboys.eshop.conversation;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nativeboys.eshop.models.MessageModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

class ConversationViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();

    private final static String CONVERSATIONS = "conversations";
    private final static String METADATA = "metadata";
    private final static String UPLOADS = "uploads";

    final static int MESSAGES_LIMIT = 10;

    public enum MessageType {
        TEXT,
        IMAGE
    }

    private DatabaseReference conversationsRef, metadataRef;

    private StorageReference storageRef;
    private final ContentResolver contentResolver;

    private final String userId, friendId;

    private final Query initQuery;
    private Query liveQuery;

    private MutableLiveData<List<MessageModel>> messages;
    private boolean fetchingDataState;

    {
        messages = new MutableLiveData<>();
        fetchingDataState = false;
    }

    private final ChildEventListener childListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.i(TAG, "onChildAdded: ");
            MessageModel message = dataSnapshot.getValue(MessageModel.class);
            String id = dataSnapshot.getKey();
            if (id == null || message == null) return;
            message.setId(id);
            List<MessageModel> messages = ConversationViewModel.this.messages.getValue();
            if (messages != null && !messages.contains(message)) {
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

    ConversationViewModel(@NonNull Application application, @NonNull String conversationId, @NonNull String userId, @NonNull String friendId) {
        super(application);
        this.userId = userId;
        this.friendId = friendId;
        contentResolver = application.getContentResolver();

        conversationsRef = FirebaseDatabase.getInstance().getReference(CONVERSATIONS).child(conversationId);
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);
        storageRef = FirebaseStorage.getInstance().getReference(UPLOADS);

        initQuery = conversationsRef.limitToLast(MESSAGES_LIMIT);
        initQuery.keepSynced(true);

        getInitData();
    }

    LiveData<List<MessageModel>> getMessages() {
        return messages;
    }

    private void sendToServer(@NonNull String text, @NonNull MessageType messageType) {
        String messageId = conversationsRef.push().getKey();
        if (messageId != null) {
            int type = (messageType == MessageType.TEXT ? 1 : 2);
            MessageModel message = new MessageModel(userId, text, new Timestamp(System.currentTimeMillis()).getTime(), type);
            // TODO: Replace with batch operations (HashMap)
            conversationsRef.child(messageId).setValue(message);
            metadataRef.child(userId).child(friendId).child("lastMessage").setValue(message);
            metadataRef.child(friendId).child(userId).child("lastMessage").setValue(message);
        }
    }

    void sendMessage(@NonNull String text) {
        sendToServer(text, MessageType.TEXT);
    }

    void sendImage(@NonNull Uri uri) {
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + getFileExtension(uri));
        fileReference.putFile(uri).continueWithTask(task -> task.isSuccessful() ? fileReference.getDownloadUrl() : null)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        sendToServer(task.getResult().toString(), MessageType.IMAGE);
                    }
                });
    }

    private void getInitData() {

        Log.i(TAG, "getInitData: ");

        fetchingDataState = true;

        initQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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

                if (messages.size() > 0) {
                    liveQuery = conversationsRef.orderByKey().startAt(messages.get(messages.size() - 1).getId());
                    liveQuery.addChildEventListener(childListener);
                }

                fetchingDataState = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fetchingDataState = false;
            }
        });
    }

    void getPreviousData() {
        if (fetchingDataState) return;
        Log.i(TAG, "getPreviousData: ");
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

    private String getFileExtension(@NonNull Uri uri) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        initQuery.keepSynced(false);
        if (liveQuery != null) liveQuery.removeEventListener(childListener);
    }
}

// https://blog.jetbrains.com/idea/2018/09/using-java-11-in-production-important-things-to-know/
// https://youtu.be/VX3UBvwJtyA