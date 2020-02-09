package com.nativeboys.eshop.paging.customerProducts;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.nativeboys.eshop.models.node.Product;

public class CustomerProductsDataSourceFactory extends DataSource.Factory<Integer, Product> {

    private final String customerId, clientId;

    public CustomerProductsDataSourceFactory(@NonNull String customerId, @NonNull String clientId) {
        this.customerId = customerId;
        this.clientId = clientId;
    }

    @NonNull
    @Override
    public DataSource<Integer, Product> create() {
        return new CustomerProductsDataSource(customerId, clientId);
    }

}
