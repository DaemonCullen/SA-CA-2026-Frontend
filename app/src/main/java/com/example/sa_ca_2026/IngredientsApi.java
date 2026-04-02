package com.example.sa_ca_2026;



import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IngredientsApi {

    @GET("api/ingredients")
    Call<List<Ingredient>> getMAllIngredients();
    @GET("api/ingredients/search/protein/{minProtein}")
    Call<List<Ingredient>> getIngredientsByMinProtein();

}
