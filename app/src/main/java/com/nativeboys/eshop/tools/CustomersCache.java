package com.nativeboys.eshop.tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nativeboys.eshop.callbacks.Completion;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.node.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomersCache {

    private static List<Customer> cachedCustomers = new ArrayList<>();

    public static void getCustomer(@NonNull String customerId, @NonNull Completion<Customer> completion) {
        for(Customer customer : cachedCustomers) {
            if (customer.getCustomerId().equals(customerId)) {
                completion.onResponse(customer);
                return;
            }
        }
        Repository.getInstance().getCustomer(customerId, new CompletionHandler<Customer>() {
            @Override
            public void onSuccess(@NonNull Customer model) {
                cachedCustomers.add(model);
                completion.onResponse(model);
            }

            @Override
            public void onFailure(@Nullable String description) {
                completion.onResponse(null);
            }
        });
    }

}
