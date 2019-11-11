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

public class SignUpFragment extends Fragment {

    interface OnLoginButtonClickListener {
        void onClick();
    }

    private FragmentActivity activity;
    private OnLoginButtonClickListener loginListener;
    private SharedViewModel viewModel;

    private EditText nameField, emailField, passwordField, confirmPassField;
    private Button registerBtn;
    private TextView loginBtn;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(activity).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameField = view.findViewById(R.id.name_field);
        emailField = view.findViewById(R.id.email_field);
        passwordField = view.findViewById(R.id.password_field);
        confirmPassField = view.findViewById(R.id.confirm_password_field);
        registerBtn = view.findViewById(R.id.register_btn);
        loginBtn = view.findViewById(R.id.login_btn);

        registerBtn.setOnClickListener(v -> {
            // TODO: implement client validation
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String name = nameField.getText().toString().trim();
            viewModel.register(email, password, name, (success, message) -> {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                // TODO: Show Error Message, or move to Menu
            });
        });

        loginBtn.setOnClickListener(v -> {
            if (loginListener != null) loginListener.onClick();
        });

    }

    void setOnLoginButtonClickListener(@NonNull OnLoginButtonClickListener listener) {
        loginListener = listener;
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
