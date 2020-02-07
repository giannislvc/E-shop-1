package com.nativeboys.eshop.viewModels;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nativeboys.eshop.callbacks.AuthCompleteListener;
import com.nativeboys.eshop.callbacks.CompletionHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FirebaseClientProvider implements FirebaseAuth.AuthStateListener {

    private static volatile FirebaseClientProvider INSTANCE = null;

    public static synchronized FirebaseClientProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseClientProvider();
        }
        return INSTANCE;
    }

    private final FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FirebaseClientProvider() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void login(@NonNull String email, @NonNull String password,
                          @NonNull AuthCompleteListener completion) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> completion.onComplete(true, null))
                .addOnFailureListener(e -> completion.onComplete(false, e.getLocalizedMessage()));
    }

    public void register(@NonNull String email, @NonNull String password,
                         @NonNull CompletionHandler<String> completion) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String id = authResult.getUser() != null ? authResult.getUser().getUid() : null;
                    if (id != null) {
                        completion.onSuccess(id);
                    } else {
                        completion.onFailure(null);
                    }
                })
                .addOnFailureListener(e -> completion.onFailure(e.getLocalizedMessage()));
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    @Nullable
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    @Nullable
    public String getFirebaseUserId() {
        return firebaseUser != null ? firebaseUser.getUid() : null;
    }

    public boolean isUserLoggedIn() {
        return firebaseUser != null;
    }

}
