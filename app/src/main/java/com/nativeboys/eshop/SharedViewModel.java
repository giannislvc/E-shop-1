package com.nativeboys.eshop;

import android.app.Application;

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
import com.nativeboys.eshop.models.MetaDataModel;
import com.nativeboys.eshop.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    // TODO: Offline overview, photos loading, take photo fragment, Authentication - Login,
    // TODO: offline - send message - another room chat - go online (check it ??)
    // TODO: new conversation offline, replace value events, DAO Firebase
    // TODO: chat offline send image
    // TODO: set up pre conversation (meta data)

    // TODO: Remove static ids
    // public final String USER_ID = "-Lr4ZsW5YBrsRbAl-09n";
    public final String USER_ID = "-Lr4ZfYW8Gig9pKXD8qL";

    private final static String USERS = "users";
    private final static String METADATA = "metadata";

    private DatabaseReference usersRef, metadataRef;

    private MutableLiveData<List<UserModel>> users;
    private MutableLiveData<List<MetaDataModel>> metaData;

    {
        users = new MutableLiveData<>();
        metaData = new MutableLiveData<>();
    }

    public SharedViewModel(@NonNull Application application) {
        super(application);
        usersRef = FirebaseDatabase.getInstance().getReference(USERS);
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);
        setUpDatabaseTriggers();
    }

    private void setUpDatabaseTriggers() {
        usersRef.addValueEventListener(new ValueEventListener() {
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
                SharedViewModel.this.users.setValue(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        metadataRef.child(USER_ID).addValueEventListener(new ValueEventListener() {
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
                SharedViewModel.this.metaData.setValue(metaData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public LiveData<List<UserModel>> getUsers() {
        return users;
    }

    public LiveData<List<MetaDataModel>> getMetaData() {
        return metaData;
    }

    public void addUser(String name, String lastName, String pickPath) {
        String id = usersRef.push().getKey();
        if (id == null) return;
        usersRef.child(id).setValue(new UserModel(name, lastName, pickPath));
    }

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

}
