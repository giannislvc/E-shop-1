package com.nativeboys.eshop.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.FormEditText;
import com.nativeboys.eshop.customViews.ToastMessage;
import com.nativeboys.eshop.viewModels.LoginViewModel;

public class SignUpFragment extends Fragment {

    private OnFragmentTransaction listener;
    private LoginViewModel loginVM;

    private ToastMessage tMessage;
    private FormEditText nameField, emailField, passwordField, confirmPassField;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginVM = new ViewModelProvider(getParentFragment() != null ?
                getParentFragment() : this).get(LoginViewModel.class);
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
        Button registerBtn = view.findViewById(R.id.register_btn);
        TextView loginBtn = view.findViewById(R.id.login_btn);

        tMessage = new ToastMessage(getActivity());
        tMessage.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 45);

        nameField.setPattern(FormEditText.USERNAME_PATTERN);
        emailField.setPattern(Patterns.EMAIL_ADDRESS);
        passwordField.setPattern(FormEditText.PASSWORD_PATTERN);
        confirmPassField.setPattern(FormEditText.PASSWORD_PATTERN);

        registerBtn.setOnClickListener(v -> register());

        loginBtn.setOnClickListener(v -> {
            if (listener != null) listener.moveToLogin();
        });

    }

    private void register() {
        boolean eValid = emailField.isValid();
        // TODO: Create endpoint in order to validate name (server side)
        boolean nValid = nameField.isValid();
        boolean pValid = passwordField.isValid();
        boolean cValid = confirmPassField.isValid();
        if (eValid && nValid && pValid && cValid) {
            String password = passwordField.getText().toString().trim();
            String cPassword = confirmPassField.getText().toString().trim();
            if (!password.equals(cPassword)) {
                confirmPassField.setState(FormEditText.State.INVALID);
            } else {
                String email = emailField.getText().toString().trim();
                String name = nameField.getText().toString().trim();
                // TODO: Create Last Name Field
                String lastName = "";
                loginVM.register(email, password, name, lastName, (success, message) -> {
                    if (success) {
                        if (listener != null) listener.moveToMainMenu();
                    } else {
                        tMessage.setText(message);
                        tMessage.show();
                    }
                });
            }
        }
    }

    void setOnFragmentTransactionListener(@NonNull OnFragmentTransaction listener) {
        this.listener = listener;
    }
}
