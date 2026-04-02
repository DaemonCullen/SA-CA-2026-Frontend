package com.example.sa_ca_2026;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MealsApi {
    @GET("api/meals")
    Call<List<Meal>> getMeals();






}
