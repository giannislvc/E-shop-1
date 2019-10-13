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
import com.nativeboys.eshop.models.MessageModel;
import com.nativeboys.eshop.models.MetaDataModel;
import com.nativeboys.eshop.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    // TODO: Offline, Paging, Photos in Chat, Authentication - Login, Register - Unregister FB Listeners,
    //  Handle new Conversation offline, handle on error

    // TODO: Remove static ids
    private final static String USER_ID = "-Lr4ZsW5YBrsRbAl-09n";

    private final static String USERS = "users";
    private final static String METADATA = "metadata";
    private final static String CONVERSATIONS = "conversations";

    private DatabaseReference usersRef, metadataRef, conversationsRef;

    private MutableLiveData<List<UserModel>> users;
    private MutableLiveData<List<MetaDataModel>> metaData;

    private LiveData<String> conversationId;
    private MutableLiveData<List<MessageModel>> conversation;

    private Query conversationQuery;
    private ValueEventListener conversationListener;

    {
        users = new MutableLiveData<>();
        metaData = new MutableLiveData<>();
        conversationId = new MutableLiveData<>();
        conversationQuery = null;
        conversationListener = null;
    }

    public SharedViewModel(@NonNull Application application) {
        super(application);
        usersRef = FirebaseDatabase.getInstance().getReference(USERS);
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);
        conversationsRef = FirebaseDatabase.getInstance().getReference(CONVERSATIONS);
        // conversation = Transformations.switchMap(conversationId, )
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

/*    private LiveData<List<MessageModel>> getConversation(String conversationId) {

        MutableLiveData<List<MessageModel>> conversation = new MutableLiveData<>();

        conversationQuery = conversationsRef.child(conversationId);

        conversationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        query.addValueEventListener(new ValueEventListener() {
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
        return conversation;
    }*/

}
