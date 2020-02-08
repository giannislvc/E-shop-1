package com.nativeboys.eshop.http;

import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.Customer;
import com.nativeboys.eshop.models.node.DeleteResponse;
import com.nativeboys.eshop.models.node.DetailedCustomer;
import com.nativeboys.eshop.models.node.DetailedProduct;
import com.nativeboys.eshop.models.node.ImageResponse;
import com.nativeboys.eshop.models.node.Like;
import com.nativeboys.eshop.models.node.LikeResponse;
import com.nativeboys.eshop.models.node.NewCustomer;
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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VMallApi {

    @GET("api/categories")
    Call<List<Category>> getCategories();

    @GET("api/searches")
    Call<List<String>> getSearches(
            @Query("text") String text
    );

    @GET("api/customers/{id}")
    Call<Customer> getCustomer(
            @Path("id") String customerId
    );
    // In order to fetch more users more frequently we can provide
    // Api which can fetch multiple users

    @GET("api/customers/{id}/productsHistory")
    Call<List<Product>> getProductHistory(
            @Path("id") String customerId,
            @Query("sort") String startLimit
    );

    @GET("api/customers/{id}/searchHistory")
    Call<List<String>> getSearchHistory(
            @Path("id") String customerId
    );

    @POST("api/customers")
    @Headers({"Content-Type: application/json"})
    Call<Customer> createCustomer(
            @Body NewCustomer request
    );

    @Multipart
    @PUT("api/customers/{id}")
    Call<Customer> updateCustomer(
            @Path("id") String customerId,
            @Part("text") RequestBody text,
            @Part MultipartBody.Part image
    );

    @GET("api/customers/{id}/details")
    Call<DetailedCustomer> getCustomerDetails(
            @Path("id") String customerId
    );

    @GET("api/products")
    Call<List<Product>> getProducts(
            @Query("customer_id") String customerId,
            @Query("filter") String filter,
            @Query("sort") String sort
    );

    @GET("api/products/{id}")
    Call<DetailedProduct> getProduct(
            @Path("id") String productId,
            @Query("customer_id") String customerId
    );

    @Multipart
    @POST("api/products/")
    Call<DetailedProduct> creteProduct(
        @Part("text") RequestBody text,
        @Part List<MultipartBody.Part> gallery
    );

    @DELETE("api/products/{id}")
    Call<DeleteResponse> deleteProduct(
            @Path("id") String productId
    );

    @POST("api/likes")
    @Headers({"Content-Type: application/json"})
    Call<LikeResponse> likeProduct(
            @Body Like request
    );

    @Multipart
    @POST("uploads")
    Call<ImageResponse> upload(
            @Part MultipartBody.Part image
    );
}
