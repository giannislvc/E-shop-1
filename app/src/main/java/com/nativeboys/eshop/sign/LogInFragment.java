package com.nativeboys.eshop.sign;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.nativeboys.eshop.R;

public class LogInFragment extends DialogFragment {

    private static final String TAG = "LogInFragment";

    private FragmentActivity activity;
    private FirebaseAuth mAuth;

    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView registerBtn;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FadeFragmentTheme);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailField = view.findViewById(R.id.email_field);
        passwordField = view.findViewById(R.id.password_field);
        loginBtn = view.findViewById(R.id.login_btn);
        registerBtn = view.findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(v -> {
            // TODO: Move to another fragment
        });

        mAuth.addAuthStateListener(firebaseAuth -> {
            String message = firebaseAuth.getCurrentUser() != null ? "Signed in" : "Sign out";
            Log.i(TAG, message);
        });

        loginBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            login(email, password);
        });
    }


    private void login(@NonNull String email, @NonNull String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                    } else if (task.getException() != null) {
                        Toast.makeText(activity, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void registerUser(@NonNull String email, @NonNull String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> Toast.makeText(activity, authResult.getUser().toString(), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}
