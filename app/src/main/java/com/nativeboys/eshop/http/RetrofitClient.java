package com.nativeboys.eshop.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {

    private static final String DEVELOPMENT_HOST = "http://192.168.1.5:5000/api/";
    private static volatile RetrofitClient INSTANCE = null;
    private final Retrofit client;

    private RetrofitClient() {
        client = new Retrofit.Builder()
                .baseUrl(DEVELOPMENT_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static synchronized RetrofitClient getInstance() {
        if(INSTANCE == null) {
            try {
                INSTANCE = new RetrofitClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    Retrofit getClient() {
        return client;
    }
}
