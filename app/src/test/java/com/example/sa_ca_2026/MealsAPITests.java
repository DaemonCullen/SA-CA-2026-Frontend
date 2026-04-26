package com.example.sa_ca_2026;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsAPITests {

    private MockWebServer mockWebServer;
    private MealsApi mealsApi;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mealsApi = retrofit.create(MealsApi.class);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getAllMeals_returnsMealList() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":1,"
                + "\"name\":\"Chicken Curry\","
                + "\"category\":\"Dinner\","
                + "\"calories\":550,"
                + "\"protein\":35,"
                + "\"difficulty\":\"Medium\""
                + "},"
                + "{"
                + "\"id\":2,"
                + "\"name\":\"Porridge\","
                + "\"category\":\"Breakfast\","
                + "\"calories\":300,"
                + "\"protein\":12,"
                + "\"difficulty\":\"Easy\""
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Meal>> response = mealsApi.getMeals().execute();

        System.out.println("Response code: " + response.code());

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(2, response.body().size());

        assertEquals("Chicken Curry", response.body().get(0).name);
        assertEquals("Dinner", response.body().get(0).category);
        assertEquals(550, response.body().get(0).calories);

        assertEquals("Porridge", response.body().get(1).name);
        assertEquals("Breakfast", response.body().get(1).category);
        assertEquals(300, response.body().get(1).calories);
    }

    @Test
    public void getMealByName_returnsMatchingMeal() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":1,"
                + "\"name\":\"Chicken Curry\","
                + "\"category\":\"Dinner\","
                + "\"calories\":550,"
                + "\"protein\":35,"
                + "\"difficulty\":\"Medium\""
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Meal>> response = mealsApi.getMealsByName("Chicken").execute();

        System.out.println("Response code: " + response.code());

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());

        assertEquals("Chicken Curry", response.body().get(0).name);
        assertEquals("Dinner", response.body().get(0).category);
    }

    @Test
    public void getMealsByCategory_returnsDinnerMeals() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":1,"
                + "\"name\":\"Chicken Curry\","
                + "\"category\":\"Dinner\","
                + "\"calories\":550,"
                + "\"protein\":35,"
                + "\"difficulty\":\"Medium\""
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Meal>> response = mealsApi.getMealsByCategory("Dinner").execute();

        System.out.println("Response code: " + response.code());

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());

        assertEquals("Chicken Curry", response.body().get(0).name);
        assertEquals("Dinner", response.body().get(0).category);
    }

    @Test
    public void getMealsByMaxCalories_returnsLowCalorieMeals() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":2,"
                + "\"name\":\"Porridge\","
                + "\"category\":\"Breakfast\","
                + "\"calories\":300,"
                + "\"protein\":12,"
                + "\"difficulty\":\"Easy\""
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Meal>> response = mealsApi.getMealsByCalories(500).execute();

        System.out.println("Response code: " + response.code());

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());

        assertEquals("Porridge", response.body().get(0).name);
        assertEquals(300, response.body().get(0).calories);
    }

    @Test
    public void createMeal_returnsCreatedMeal() throws Exception {
        String fakeJsonResponse = "{"
                + "\"id\":3,"
                + "\"name\":\"Test Meal\","
                + "\"category\":\"Lunch\","
                + "\"calories\":450,"
                + "\"protein\":25,"
                + "\"difficulty\":\"Easy\""
                + "}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Meal newMeal = new Meal();
        newMeal.name = "Test Meal";
        newMeal.category = "Lunch";
        newMeal.calories = 450;
        newMeal.protein = 25;
        newMeal.difficulty = "Easy";

        Response<Meal> response = mealsApi.createMeal(newMeal).execute();

        System.out.println("Response code: " + response.code());

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        assertEquals(3, response.body().id);
        assertEquals("Test Meal", response.body().name);
        assertEquals("Lunch", response.body().category);
        assertEquals(450, response.body().calories);
    }

    @Test
    public void deleteMeal_returnsSuccessfulResponse() throws Exception {
        String fakeJsonResponse = "{"
                + "\"id\":1,"
                + "\"name\":\"Deleted Meal\","
                + "\"category\":\"Dinner\","
                + "\"calories\":500,"
                + "\"protein\":30,"
                + "\"difficulty\":\"Medium\""
                + "}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<Meal> response = mealsApi.deleteMeal(1).execute();

        System.out.println("Response code: " + response.code());

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        assertEquals(1, response.body().id);
        assertEquals("Deleted Meal", response.body().name);
    }
}