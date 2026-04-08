package com.example.sa_ca_2026;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MealsApi {

    @GET("api/meals")
    Call<List<Meal>> getMeals();

    @GET("api/meals/{id}")
    Call<Meal> getMealById(@Path("id") int id);

    @GET("api/meals/search/name/{name}")
    Call<List<Meal>> getMealsByName(@Path("name") String name);

    @GET("api/meals/search/category/{category}")
    Call<List<Meal>> getMealsByCategory(@Path("category") String category);

    @GET("api/meals/search/totalFat/{totalFat}")
    Call<List<Meal>> getMealsByTotalFat(@Path("totalFat") double totalFat);

    @GET("api/meals/search/rating/{rating}")
    Call<List<Meal>> getMealsByRating(@Path("rating") double rating);

    @GET("api/meals/search/difficulty/{difficulty}")
    Call<List<Meal>> getMealsByDifficulty(@Path("difficulty") String difficulty);

    @GET("api/meals/search/prepTime/{prepTime}")
    Call<List<Meal>> getMealsByPrepTime(@Path("prepTime") int prepTime);

    @GET("api/meals/search/cookTime/{cookTime}")
    Call<List<Meal>> getMealsByCookTime(@Path("cookTime") int cookTime);

    @GET("api/meals/search/servings/{servings}")
    Call<List<Meal>> getMealsByServings(@Path("servings") int servings);

    @GET("api/meals/search/calories/{calories}")
    Call<List<Meal>> getMealsByCalories(@Path("calories") double calories);

    @GET("api/meals/search/protein/{protein}")
    Call<List<Meal>> getMealsByProtein(@Path("protein") double protein);

    // Fixed path variable to match the URL placeholder
    @GET("api/meals/search/minprotein/{minProtein}")
    Call<List<Meal>> getMealsByMinProtein(@Path("minProtein") double minProtein);

    @POST("api/meals")
    Call<Meal> createMeal(@Body Meal meal);

    @PUT("api/meals/{id}")
    Call<Meal> updateMeal(@Path("id") int id, @Body Meal updatedMeal);

    @DELETE("api/meals/{id}")
    Call<Meal> deleteMeal(@Path("id") int id);
}
