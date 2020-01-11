package com.nativeboys.eshop.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PageKeyedDataSource;

import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;

import java.util.List;

public class ProductsDataSource extends PageKeyedDataSource<Integer, Product> {

    private Sort sort;
    private Filter filter;
    private String customerId;

    ProductsDataSource(@NonNull String customerId, @NonNull Filter filter, @NonNull Sort sort) {
        this.filter = filter;
        this.customerId = customerId;
        this.sort = sort;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {
        Repository.getInstance().getProducts(customerId, filter, sort, new CompletionHandler<List<Product>>() {
            @Override
            public void onSuccess(@NonNull List<Product> model) {
                callback.onResult(model, null,
                        sort.getLimit() == model.size() ? model.size() : null);
            }

            @Override
            public void onFailure(@Nullable String description) { }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) { }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {
        sort.setStart(params.key);
        Repository.getInstance().getProducts(customerId, filter, sort, new CompletionHandler<List<Product>>() {
            @Override
            public void onSuccess(@NonNull List<Product> model) {
                Integer key = null;
                if (sort.getLimit() == model.size()) {
                    key = params.key + sort.getLimit();
                }
                callback.onResult(model, key);

            }

            @Override
            public void onFailure(@Nullable String description) { }
        });
    }
}
