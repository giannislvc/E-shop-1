package com.nativeboys.eshop.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.viewModels.SharedViewModel;

public class SignInFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    interface OnRegisterButtonClickListener {
        void onClick();
    }

    private FragmentActivity activity;
    private OnRegisterButtonClickListener registerListener;
    private SharedViewModel viewModel;

    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView registerBtn;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(activity).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailField = view.findViewById(R.id.email_field);
        passwordField = view.findViewById(R.id.password_field);
        loginBtn = view.findViewById(R.id.login_btn);
        registerBtn = view.findViewById(R.id.register_btn);

        loginBtn.setOnClickListener(v -> {
            // TODO: implement client validation
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            viewModel.login(email, password, (success, message) -> {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                // TODO: Show Error Message, or move to Menu
            });
        });

        registerBtn.setOnClickListener(v -> {
            if (registerListener != null) registerListener.onClick();
        });

    }

    void setOnRegisterButtonClickListener(@NonNull OnRegisterButtonClickListener listener) {
        registerListener = listener;
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
