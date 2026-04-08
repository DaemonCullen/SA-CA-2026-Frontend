package com.example.sa_ca_2026;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlansApi {

    @GET("api/plans")
    Call<List<Plan>> getAllPlans();

    @GET("api/plans/{id}")
    Call<Plan> getPlanById(@Path("id") int id);

    @GET("api/plans/{id}/meals")
    Call<List<Meal>> getMealsByPlan(@Path("id") int id);

    @POST("api/plans")
    Call<Plan> createPlan(@Body Plan plan);

    @PUT("api/plans/{id}")
    Call<Plan> updatePlan(@Path("id") int id, @Body Plan updatedPlan);

    @DELETE("api/plans/{id}")
    Call<Plan> deletePlan(@Path("id") int id);

    @POST("api/plans/{planId}/meals/{mealId}")
    Call<Meal> assignMealToPlan(@Path("planId") int planId, @Path("mealId") int mealId);

    @DELETE("api/plans/{planId}/meals/{mealId}")
    Call<Meal> removeMealFromPlan(@Path("planId") int planId, @Path("mealId") int mealId);
}
