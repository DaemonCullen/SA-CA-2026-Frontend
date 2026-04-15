package com.example.sa_ca_2026;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // local version
    // private static final String BASE_URL = "http://10.0.2.2:5228/";

   // azure version
   private static final String BASE_URL = "https://marksapi-d7fvaucyewh4excm.canadaeast-01.azurewebsites.net/";


    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
