package com.nativeboys.eshop.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nativeboys.eshop.AuthCompleteListener;
import com.nativeboys.eshop.Completion;
import com.nativeboys.eshop.models.MetaDataModel;
import com.nativeboys.eshop.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class GlobalViewModel extends AndroidViewModel {

    // TODO: set up pre conversation (meta data)
    // TODO: Offline overview, take photo fragment,
    // TODO: chat_png offline send image

    // TODO: offline - send message - another room chat_png - go online (check it ??)
    // TODO: new conversation offline, replace value events, DAO Firebase

    private final static String USERS = "users";
    final static String METADATA = "metadata";
    final static String CONVERSATIONS = "conversations";

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, metadataRef, conversationsRef;

    private String userId;
    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<List<MetaDataModel>> metaData;

    private MutableLiveData<List<UserModel>> users;

    {
        user = new MutableLiveData<>();
        metaData = new MutableLiveData<>();

        users = new MutableLiveData<>();
    }

    private ValueEventListener usersListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<UserModel> users = new ArrayList<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                String id = userSnapshot.getKey();
                if (id != null && userId != null && !userId.equals(id)) {
                    UserModel user = userSnapshot.getValue(UserModel.class);
                    if (user != null) {
                        user.setId(id);
                        users.add(user);
                    }
                }
            }
            GlobalViewModel.this.users.setValue(users);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private ValueEventListener metaDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<MetaDataModel> metaData = new ArrayList<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                String id = userSnapshot.getKey();
                if (id == null) continue;
                MetaDataModel meta = userSnapshot.getValue(MetaDataModel.class);
                if (meta != null) {
                    meta.setId(id);
                    metaData.add(meta);
                }
            }
            GlobalViewModel.this.metaData.setValue(metaData);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    public GlobalViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference(USERS);
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);
        conversationsRef = FirebaseDatabase.getInstance().getReference(CONVERSATIONS);

        mAuth.addAuthStateListener(firebaseAuth -> {
            setListeners(false);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                this.user.setValue(user);
                userId = user.getUid();
                setListeners(true);
            }
        });
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public LiveData<List<MetaDataModel>> getMetaData() {
        return metaData;
    }

    public LiveData<List<UserModel>> getUsers() {
        return users;
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void registerUser(@NonNull String email, @NonNull String password, @NonNull String name, @NonNull AuthCompleteListener listener) {
        // TODO: Apply User inside Database
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getLocalizedMessage()));
    }

    public void loginUser(@NonNull String email, @NonNull String password, @NonNull AuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getLocalizedMessage()));
    }

    public void signOut() {
        mAuth.signOut();
    }

    private void setListeners(boolean enable) {
        FirebaseUser user = getUser().getValue();
        if (user == null) return;
        String userId = user.getUid();
        if (enable) {
            usersRef.addValueEventListener(usersListener);
            metadataRef.child(userId).addValueEventListener(metaDataListener);
        } else {
            usersRef.removeEventListener(usersListener);
            metadataRef.child(userId).removeEventListener(metaDataListener);
        }
    }

/*    public void addUser(String name, String lastName, String pickPath) {
        String id = usersRef.push().getKey();
        if (id == null) return;
        usersRef.child(id).setValue(new UserModel(name, lastName, pickPath));
    }*/

    public void getConversationIdWith(@NonNull String friendId, @NonNull Completion<String> completion) {
        getExistingConversationId(friendId, model ->
                completion.onResponse(model != null ? model : createConversation(friendId)));
    }

    private void getExistingConversationId(@NonNull String friendId, @NonNull Completion<String> completion) {
        if (userId != null) {
            metadataRef.child(userId).child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MetaDataModel model = dataSnapshot.getValue(MetaDataModel.class);
                    completion.onResponse(model != null ? model.getConversationId() : null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    completion.onResponse(null);
                }
            });
        } else {
            completion.onResponse(null);
        }
    }

    @Nullable
    private String createConversation(String friendId) {
        // String conId = metadataRef.child(USER_ID).child(friendId).getKey();
        if (userId == null) return null;
        String conId = conversationsRef.push().getKey();
        MetaDataModel sharedMeta = new MetaDataModel(conId, null);
        if (conId != null) {
            metadataRef.child(userId).child(friendId).setValue(sharedMeta);
            metadataRef.child(friendId).child(userId).setValue(sharedMeta);
        }
        return conId;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        setListeners(false);
    }
}
