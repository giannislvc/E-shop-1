package com.nativeboys.eshop.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {

    private static final String DEVELOPMENT_HOST = "http://192.168.1.4:5000/";
    private static volatile RetrofitClient INSTANCE = null;
    private final Retrofit client;

    private static String getBaseUrl() {
        return DEVELOPMENT_HOST + "api/";
    }

    public static String getUploadsUrl() {
        return DEVELOPMENT_HOST + "uploads/";
    }

    private RetrofitClient() {
        client = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
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
