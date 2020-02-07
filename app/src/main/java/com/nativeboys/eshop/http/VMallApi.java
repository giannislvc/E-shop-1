package com.nativeboys.eshop.http;

import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.Customer;
import com.nativeboys.eshop.models.node.DeleteResponse;
import com.nativeboys.eshop.models.node.DetailedProduct;
import com.nativeboys.eshop.models.node.Like;
import com.nativeboys.eshop.models.node.LikeResponse;
import com.nativeboys.eshop.models.node.Product;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VMallApi {

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("searches")
    Call<List<String>> getSearches(
            @Query("text") String text
    );

    @GET("customer/{id}")
    Call<Customer> getCustomer();

    @GET("customers/{id}/productsHistory")
    Call<List<Product>> getProductHistory(
            @Path("id") String customerId,
            @Query("sort") String startLimit
    );

    @GET("customers/{id}/searchHistory")
    Call<List<String>> getSearchHistory(
            @Path("id") String customerId
    );

    @GET("products")
    Call<List<Product>> getProducts(
            @Query("customer_id") String customerId,
            @Query("filter") String filter,
            @Query("sort") String sort
    );

    @GET("products/{id}")
    Call<DetailedProduct> getProduct(
            @Path("id") String productId,
            @Query("customer_id") String customerId
    );

    @Multipart
    @POST("products/2")
    Call<DetailedProduct> creteProduct(
        @Part("text") RequestBody text,
        @Part List<MultipartBody.Part> gallery
    );

    @DELETE("products/{id}")
    Call<DeleteResponse> deleteProduct(
            @Path("id") String productId
    );

    @POST("likes")
    @Headers({"Content-Type: application/json"})
    Call<LikeResponse> likeProduct(
            @Body Like request
    );
}
