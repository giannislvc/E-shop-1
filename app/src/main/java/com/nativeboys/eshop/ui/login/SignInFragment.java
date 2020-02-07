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

public class SignInFragment extends Fragment {

    private OnFragmentTransaction listener;
    private LoginViewModel loginVM;

    private FormEditText emailField, passwordField;
    private ToastMessage tMessage;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginVM = new ViewModelProvider(getParentFragment() != null ?
                getParentFragment() : this).get(LoginViewModel.class);
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
        Button loginBtn = view.findViewById(R.id.login_btn);
        TextView registerBtn = view.findViewById(R.id.register_btn);

        tMessage = new ToastMessage(getActivity());
        tMessage.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 45);

        emailField.setPattern(Patterns.EMAIL_ADDRESS);
        passwordField.setPattern(FormEditText.PASSWORD_PATTERN);

        loginBtn.setOnClickListener(v -> login());

        registerBtn.setOnClickListener(v -> {
            if (listener != null) listener.moveToRegister();
        });
    }

    private void login() {
        boolean eValid = emailField.isValid();
        boolean pValid = passwordField.isValid();
        if (eValid && pValid) {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            loginVM.login(email, password, (success, message) -> {
                if (success) {
                    if (listener != null) listener.moveToMainMenu();
                } else {
                    tMessage.setText(message);
                    tMessage.show();
                }
            });
        }
    }

    void setOnFragmentTransactionListener(@NonNull OnFragmentTransaction listener) {
        this.listener = listener;
    }
}
