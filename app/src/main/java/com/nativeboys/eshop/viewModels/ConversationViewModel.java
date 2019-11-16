package com.nativeboys.eshop.viewModels;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

import static com.nativeboys.eshop.viewModels.GlobalViewModel.CONVERSATIONS;
import static com.nativeboys.eshop.viewModels.GlobalViewModel.METADATA;

public class ConversationViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();

    public final static int MESSAGES_PER_FETCH = 10;
    private final static String UPLOADS = "uploads";

    public enum MessageType {
        TEXT,
        IMAGE
    }

    private DatabaseReference conversationsRef, metadataRef;

    private StorageReference storageRef;
    private final ContentResolver contentResolver;

    private final String userId, friendId;
    private int count;

    private MutableLiveData<List<MessageModel>> messages;
    private boolean fetchingDataState;

    {
        messages = new MutableLiveData<>();
        count = 0;
    }

    private Query liveQuery;

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.i(TAG, "Listener Triggered: " + dataSnapshot.getChildrenCount());
            List<MessageModel> messages = new ArrayList<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                MessageModel message = userSnapshot.getValue(MessageModel.class);
                String id = userSnapshot.getKey();
                if (id == null || message == null) continue;
                message.setId(id);
                messages.add(message);
            }
            ConversationViewModel.this.messages.setValue(messages);
            fetchingDataState = false;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.i(TAG, "onCancelled: ");
            // TODO: implement
            fetchingDataState = false;
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

        conversationsRef.keepSynced(true);

        liveQuery = conversationsRef.limitToLast(MESSAGES_PER_FETCH);
        fetchingDataState = true;
        liveQuery.addValueEventListener(valueEventListener);
    }

    public synchronized void getPreviousMessages() {
        Log.i(TAG, "getPreviousMessages: ");
        if (fetchingDataState) return;
        fetchingDataState = true;
        liveQuery.removeEventListener(valueEventListener);
        if (messages.getValue() != null) count = messages.getValue().size();
        count += MESSAGES_PER_FETCH;
        liveQuery = conversationsRef.limitToLast(count);
        liveQuery.addValueEventListener(valueEventListener);
    }

    public LiveData<List<MessageModel>> getMessages() {
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

    public void sendMessage(@NonNull String text) {
        sendToServer(text, MessageType.TEXT);
    }

    public void sendImage(@NonNull Uri uri) {
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + getFileExtension(uri));
        fileReference.putFile(uri).continueWithTask(task -> task.isSuccessful() ? fileReference.getDownloadUrl() : null)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        sendToServer(task.getResult().toString(), MessageType.IMAGE);
                    }
                });
    }

    private String getFileExtension(@NonNull Uri uri) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onCleared() {
        liveQuery.removeEventListener(valueEventListener);
        conversationsRef.keepSynced(false);
    }
}

// https://blog.jetbrains.com/idea/2018/09/using-java-11-in-production-important-things-to-know/
// https://youtu.be/VX3UBvwJtyA