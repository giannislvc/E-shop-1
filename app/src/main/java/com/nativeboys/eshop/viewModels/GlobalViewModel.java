package com.nativeboys.eshop.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    // TODO: Remove static ids
    public final String USER_ID = "-Lr4ZsW5YBrsRbAl-09n";
    //public final String USER_ID = "16T4SQ1zY5RjQlSsAwylIflW1fO2";

    private final String TAG = getClass().getSimpleName();

    private final static String USERS = "users";
    private final static String METADATA = "metadata";

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, metadataRef;

    private MutableLiveData<FirebaseUser> user; // LoggedIn User
    private MutableLiveData<List<UserModel>> users;
    private MutableLiveData<List<MetaDataModel>> metaData;

    {
        user = new MutableLiveData<>();
        users = new MutableLiveData<>();
        metaData = new MutableLiveData<>();
    }

    private ValueEventListener usersListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<UserModel> users = new ArrayList<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                String id = userSnapshot.getKey();
                if (id == null) continue;
                UserModel user = userSnapshot.getValue(UserModel.class);
                if (user != null) {
                    user.setId(id);
                    users.add(user);
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

        mAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            this.user.setValue(user);
            removeListeners();
            if (user != null) addListeners();
            Log.i(TAG, user != null ? "Signed in" : "Sign out");
        });
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<List<UserModel>> getUsers() {
        return users;
    }

    public LiveData<List<MetaDataModel>> getMetaData() {
        return metaData;
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void register(@NonNull String email, @NonNull String password, @NonNull String name, @NonNull AuthCompleteListener listener) {
        // TODO: Apply User inside Database
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getLocalizedMessage()));
    }

    public void login(@NonNull String email, @NonNull String password, @NonNull AuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getLocalizedMessage()));
    }

    public void signOut() {
        mAuth.signOut();
    }

    private void addListeners() {
        usersRef.addValueEventListener(usersListener);
        metadataRef.child(USER_ID).addValueEventListener(metaDataListener);
    }

    private void removeListeners() {
        usersRef.removeEventListener(usersListener);
        metadataRef.child(USER_ID).removeEventListener(metaDataListener);
    }

/*    public void addUser(String name, String lastName, String pickPath) {
        String id = usersRef.push().getKey();
        if (id == null) return;
        usersRef.child(id).setValue(new UserModel(name, lastName, pickPath));
    }*/

    public void getConversationIdWith(String friendId, Completion<String> completion) {
        fetchConversationId(friendId, model -> {
            if (model == null) {
                completion.onResponse(setUpNewConversation(friendId));
            } else {
                completion.onResponse(model);
            }
        });
    }

    private String setUpNewConversation(String friendId) {
        String conId = metadataRef.child(USER_ID).child(friendId).getKey(); // TODO: Check it
        MetaDataModel sharedMeta = new MetaDataModel(conId, null);
        if (conId != null) {
            metadataRef.child(USER_ID).child(friendId).setValue(sharedMeta);
            metadataRef.child(friendId).child(USER_ID).setValue(sharedMeta);
        }
        return conId;
    }

    private void fetchConversationId(String friendId, Completion<String> completion) {
        Query query = metadataRef.child(USER_ID).child(friendId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        removeListeners();
    }
}