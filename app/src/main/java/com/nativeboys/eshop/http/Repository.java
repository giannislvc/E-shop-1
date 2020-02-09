package com.nativeboys.eshop.http;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.Customer;
import com.nativeboys.eshop.models.node.DeleteResponse;
import com.nativeboys.eshop.models.node.DetailedCustomer;
import com.nativeboys.eshop.models.node.DetailedProduct;
import com.nativeboys.eshop.models.node.ImageResponse;
import com.nativeboys.eshop.models.node.Like;
import com.nativeboys.eshop.models.node.LikeResponse;
import com.nativeboys.eshop.models.node.NewCustomer;
import com.nativeboys.eshop.models.node.NewProduct;
import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;
import com.nativeboys.eshop.models.query.StartLimit;
import com.nativeboys.eshop.tools.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getProduct(String productId, String customerId, final CompletionHandler<DetailedProduct> completion) {
        Call<DetailedProduct> call = api.getProduct(productId, customerId);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getProducts(String customerId, Filter filter, Sort sort, final CompletionHandler<List<Product>> completion) {
        Log.i(TAG, "Customer: " + customerId + " Filter: " + filter.toString() + " Sort: "+ sort.toString());
        Call<List<Product>> call = api.getProducts(customerId, gsonHelper.toJson(filter), gsonHelper.toJson(sort));
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getCustomer(@NonNull String customerId, CompletionHandler<Customer> completion) {
        Call<Customer> call = api.getCustomer(customerId);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getCustomerDetails(String customerId, CompletionHandler<DetailedCustomer> completion) {
        Call<DetailedCustomer> call = api.getCustomerDetails(customerId);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getCustomerProductsHistory(String customerId, StartLimit startLimit, CompletionHandler<List<Product>> completion) {
        Call<List<Product>> call = api.getCustomerProductsHistory(customerId, gsonHelper.toJson(startLimit));
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getCustomerSearchHistory(String customerId, CompletionHandler<List<String>> completion) {
        Call<List<String>> call = api.getCustomerSearchHistory(customerId);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getCustomerProducts(String customerId, String clientId, Filter filter, Sort sort, CompletionHandler<List<Product>> completion) {
        Call<List<Product>> call = api.getCustomerProducts(customerId, clientId, gsonHelper.toJson(filter), gsonHelper.toJson(sort));
        request(call, completion);
    }

    @EverythingIsNonNull
    public void getSearches(String text, CompletionHandler<List<String>> completion) {
        Call<List<String>> call = api.getSearches(text);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void likeProduct(String customerId, String productId, CompletionHandler<LikeResponse> completion) {
        Call<LikeResponse> call = api.likeProduct(new Like(customerId, productId));
        request(call, completion);
    }

    @EverythingIsNonNull
    public void deleteProduct(String productId, CompletionHandler<DeleteResponse> completion) {
        Call<DeleteResponse> call = api.deleteProduct(productId);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void createProduct(Context context, NewProduct product, List<Uri> uris, CompletionHandler<DetailedProduct> completion) {
        RequestBody textPart = createPartFromString(gsonHelper.toJson(product));
        List<MultipartBody.Part> galleryPart = new ArrayList<>();
        for (Uri uri : uris) {
            galleryPart.add(prepareFilePart(context, "gallery", uri));
        }
        Call<DetailedProduct> call = api.creteProduct(textPart, galleryPart);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void updateProduct(Context context, String productId,  NewProduct product, List<Uri> uris, CompletionHandler<DetailedProduct> completion) {
        RequestBody textPart = createPartFromString(gsonHelper.toJson(product));
        List<MultipartBody.Part> galleryPart = new ArrayList<>();
        for (Uri uri : uris) {
            galleryPart.add(prepareFilePart(context, "gallery", uri));
        }
        Call<DetailedProduct> call = api.updateProduct(productId, textPart, galleryPart);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void upload(Context context, Uri uri, CompletionHandler<ImageResponse> completion) {
        MultipartBody.Part part = prepareFilePart(context, "image", uri);
        Call<ImageResponse> call = api.upload(part);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void createCustomer(NewCustomer customer, CompletionHandler<Customer> completion) {
        Call<Customer> call = api.createCustomer(customer);
        request(call, completion);
    }

    @EverythingIsNonNull
    public void updateCustomer(Context context, Uri uri, NewCustomer customer, CompletionHandler<Customer> completion) {
        RequestBody textPart = createPartFromString(gsonHelper.toJson(customer));
        MultipartBody.Part part = prepareFilePart(context, "image", uri);
        Call<Customer> call = api.updateCustomer(customer.getCustomerId(), textPart, part);
        request(call, completion);
    }

    @NonNull
    private RequestBody createPartFromString(String text) {
        return RequestBody.create(MultipartBody.FORM, text);
    }

    @Nullable
    private MultipartBody.Part prepareFilePart(@NonNull Context context, String partName, Uri fileUri) {
        // get actual file from uri
        File file = FileUtils.getFile(context, fileUri);
        // create RequestBody instance from file
        String type = context.getContentResolver().getType(fileUri);
        if (type != null) {
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(type),
                    file
            );
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }
        return null;
    }

    @EverythingIsNonNull
    private <T> void request(Call<T> call, CompletionHandler<T> completion) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Log.i(TAG, "onResponse: " + response);
                responseHandler(response, completion);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
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
