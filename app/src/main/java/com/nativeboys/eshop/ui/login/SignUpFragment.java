package com.nativeboys.eshop.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.FormEditText;
import com.nativeboys.eshop.customViews.ToastMessage;
import com.nativeboys.eshop.viewModels.SharedViewModel;

public class SignUpFragment extends Fragment {

    interface OnLoginButtonClickListener {
        void onClick();
    }

    private FragmentActivity activity;
    private OnLoginButtonClickListener loginListener;
    private SharedViewModel viewModel;

    private ToastMessage tMessage;

    private FormEditText nameField, emailField, passwordField, confirmPassField;
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

        tMessage = new ToastMessage(activity);
        tMessage.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 45);

        registerBtn.setOnClickListener(v -> register());

        loginBtn.setOnClickListener(v -> {
            if (loginListener != null) loginListener.onClick();
        });

    }

    private void register() {
        if (emailField.isValid() && nameField.isValid() && passwordField.isValid()) {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String name = nameField.getText().toString().trim();
            viewModel.register(email, password, name, (success, message) -> {
                if (success) {
                    // TODO: Move to main menu
                } else {
                    tMessage.setText(message);
                    tMessage.show();
                }
            });
        }
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
