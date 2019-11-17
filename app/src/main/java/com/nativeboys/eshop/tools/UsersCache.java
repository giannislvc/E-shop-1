package com.nativeboys.eshop.tools;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nativeboys.eshop.Completion;
import com.nativeboys.eshop.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersCache {

    private final static DatabaseReference database =
            FirebaseDatabase.getInstance().getReference("users");

    private static List<UserModel> cachedUsers = new ArrayList<>();

    public static void getUser(@NonNull String userId, @NonNull Completion<UserModel> completion) {
        for (UserModel user : cachedUsers) {
            if (user.getId().equals(userId)) {
                completion.onResponse(user);
                return;
            }
        }
        database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                String id = dataSnapshot.getKey();
                if (model != null && id != null) {
                    model.setId(id);
                    cachedUsers.add(model);
                    completion.onResponse(model);
                } else {
                    completion.onResponse(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                completion.onResponse(null);
            }
        });
    }

}
