package com.nativeboys.eshop.http;

import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.DetailedProduct;
import com.nativeboys.eshop.models.node.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VMallApi {

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("products/{id}")
    Call<DetailedProduct> getProduct(
            @Path("id") String productId,
            @Query("customer_id") String customerId);

    @GET("products")
    Call<List<Product>> getProducts(
            @Query("customer_id") String customerId,
            @Query("filter") String filter,
            @Query("sort") String sort);

    @GET("customers/{id}/productsHistory")
    Call<List<Product>> getProductHistory(
            @Path("id") String customerId,
            @Query("sort") String startLimit);

    @GET("customers/{id}/searchHistory")
    Call<List<String>> getSearchHistory(
            @Path("id") String customerId);

    @GET("searches/")
        Call<List<String>> getSearches(
                @Query("text") String text);

}
