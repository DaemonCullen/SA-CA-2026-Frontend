package com.example.sa_ca_2026;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:5228/";
    private static Retrofit retrofit;
    private static String customBaseUrl;

    public static Retrofit getClient() {
        if (retrofit == null) {
            String url = customBaseUrl != null ? customBaseUrl : BASE_URL;
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void setCustomBaseUrl(String url) {
        customBaseUrl = url;
        retrofit = null; // Recreate retrofit with new URL
    }

    public static void reset() {
        customBaseUrl = null;
        retrofit = null;
    }
}
