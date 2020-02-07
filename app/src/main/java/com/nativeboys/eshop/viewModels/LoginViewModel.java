package com.nativeboys.eshop.viewModels;

import android.app.Application;

import com.nativeboys.eshop.callbacks.AuthCompleteListener;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.node.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {

    private final Repository repository;
    private final FirebaseClientProvider clientProvider;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        clientProvider = FirebaseClientProvider.getInstance();
    }

    public void login(@NonNull String email, @NonNull String password,
                      @NonNull AuthCompleteListener completion) {
        clientProvider.login(email, password, completion);
    }

    public void register(@NonNull String email, @NonNull String password,
                         @NonNull String firstName, @NonNull String lastName,
                         @NonNull AuthCompleteListener completion) {
        clientProvider.register(email, password, (success, message) -> {
            if (success) {
                repository.createCustomer(email, password, firstName, lastName, new CompletionHandler<Customer>() {
                    @Override
                    public void onSuccess(@NonNull Customer model) {
                        completion.onComplete(true, null);
                    }

                    @Override
                    public void onFailure(@Nullable String description) {
                        completion.onComplete(false, description);
                    }
                });
            }
            completion.onComplete(success, message);
        });
    }

    public boolean isUserLoggedIn() {
        return clientProvider.isUserLoggedIn();
    }

}
