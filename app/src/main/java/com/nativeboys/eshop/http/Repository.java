package com.nativeboys.eshop.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.DetailedProduct;
import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;
import com.nativeboys.eshop.models.query.StartLimit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class Repository {

    private final String TAG = getClass().getSimpleName();
    private static volatile Repository INSTANCE = null;

    private final VMallApi api;
    private final Gson gsonHelper;

    public static synchronized Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    private Repository() {
        api = RetrofitClient.getInstance().getClient().create(VMallApi.class);
        gsonHelper = new Gson();
    }

    @EverythingIsNonNull
    public void getCategories(final CompletionHandler<List<Category>> completion) {
        Call<List<Category>> call = api.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    completion.onSuccess(response.body());
                } else {
                    completion.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                completion.onFailure(t.getMessage());
            }
        });
    }

    @EverythingIsNonNull
    public void getProduct(String productId, String customerId, final CompletionHandler<DetailedProduct> completion) {
        Call<DetailedProduct> call = api.getProduct(productId, customerId);
        call.enqueue(new Callback<DetailedProduct>() {
            @Override
            public void onResponse(Call<DetailedProduct> call, Response<DetailedProduct> response) {
                if (response.isSuccessful() && response.body() != null) {
                    completion.onSuccess(response.body());
                } else {
                    completion.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<DetailedProduct> call, Throwable t) {
                completion.onFailure(t.getMessage());
            }
        });
    }

    @EverythingIsNonNull
    public void getProducts(String customerId, Filter filter, Sort sort, final CompletionHandler<List<Product>> completion) {
        Log.i(TAG, "Customer: " + customerId + " Filter: " + filter.toString() + " Sort: "+ sort.toString());
        Call<List<Product>> call = api.getProducts(customerId, gsonHelper.toJson(filter), gsonHelper.toJson(sort));
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                responseHandler(response, completion);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                completion.onFailure(t.getMessage());
            }
        });
    }

    @EverythingIsNonNull
    public void getProductHistory(String customerId, StartLimit startLimit, CompletionHandler<List<Product>> completion) {
        Call<List<Product>> call = api.getProductHistory(customerId, gsonHelper.toJson(startLimit));
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                responseHandler(response, completion);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                completion.onFailure(t.getMessage());
            }
        });
    }

    @EverythingIsNonNull
    public void getSearchHistory(String customerId, CompletionHandler<List<String>> completion) {
        Call<List<String>> call = api.getSearchHistory(customerId);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                responseHandler(response, completion);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                completion.onFailure(t.getMessage());
            }
        });
    }

    private <T> void responseHandler(@NonNull Response<T> response, CompletionHandler<T> completion) {
        if (response.isSuccessful() && response.body() != null) {
            completion.onSuccess(response.body());
        } else {
            completion.onFailure(null);
        }
    }

}
