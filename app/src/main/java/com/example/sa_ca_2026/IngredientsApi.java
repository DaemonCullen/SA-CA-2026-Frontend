package com.example.sa_ca_2026;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IngredientsApi {

    @GET("api/ingredients")
    Call<List<Ingredient>> getAllIngredients();

    @GET("api/ingredients/{id}")
    Call<Ingredient> getIngredientById(@Path("id") int id);

    // search methods

    @GET("api/ingredients/search/name/{name}")
    Call<List<Ingredient>> getIngredientByName(@Path("name") String name);

    @GET("api/ingredients/search/origin/{origin}")
    Call<List<Ingredient>> getIngredientsByOrigin(@Path("origin") String origin);

    @GET("api/ingredients/search/price/{maxPrice}")
    Call<List<Ingredient>> getIngredientsByMaxPrice(@Path("maxPrice") double maxPrice);

    @GET("api/ingredients/search/protein/{minProtein}")
    Call<List<Ingredient>> getIngredientsByMinProtein(@Path("minProtein") double minProtein);

    @GET("api/ingredients/search/fat/{minFat}")
    Call<List<Ingredient>> getIngredientsByMinFat(@Path("minFat") double minFat);

    // filtering

    @GET("api/ingredients/organic")
    Call<List<Ingredient>> getOrganic(@Query("isOrganic") Boolean isOrganic);

    @GET("api/ingredients/isOrganic/{isOrganic}")
    Call<List<Ingredient>> getByIsOrganic(@Path("isOrganic") boolean isOrganic);

    @GET("api/ingredients/page")
    Call<List<Ingredient>> getPaged(@Query("page") int page, @Query("pageSize") int pageSize);

    // CRUD

    @POST("api/ingredients")
    Call<Ingredient> addIngredient(@Body Ingredient newIngredient);

    @PUT("api/ingredients/{id}")
    Call<Ingredient> updateIngredient(@Path("id") int id, @Body Ingredient updatedIngredient);

    @PUT("api/ingredients/simple-update")
    Call<Ingredient> simpleUpdateIngredient(@Query("id") int id, @Body Ingredient simpleUpdatedIngredient);

    @DELETE("api/ingredients/{id}")
    Call<Void> removeIngredient(@Path("id") int id);
}
