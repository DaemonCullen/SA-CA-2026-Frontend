package com.example.sa_ca_2026;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientsAPITests {

    private MockWebServer mockWebServer;
    private IngredientsApi ingredientsApi;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        ingredientsApi = retrofit.create(IngredientsApi.class);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getAllIngredients_returnsIngredientList() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":1,"
                + "\"name\":\"Chicken Breast\","
                + "\"origin\":\"Ireland\","
                + "\"price\":5.99,"
                + "\"protein\":31,"
                + "\"fat\":3.6,"
                + "\"organic\":false"
                + "},"
                + "{"
                + "\"id\":2,"
                + "\"name\":\"Eggs\","
                + "\"origin\":\"Ireland\","
                + "\"price\":3.50,"
                + "\"protein\":13,"
                + "\"fat\":11,"
                + "\"organic\":true"
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Ingredient>> response = ingredientsApi.getAllIngredients().execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(2, response.body().size());

        assertEquals("Chicken Breast", response.body().get(0).name);
        assertEquals("Ireland", response.body().get(0).origin);
        assertEquals(5.99, response.body().get(0).price, 0.01);

        assertEquals("Eggs", response.body().get(1).name);
    }

    @Test
    public void getIngredientByName_returnsMatchingIngredient() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":1,"
                + "\"name\":\"Chicken Breast\","
                + "\"origin\":\"Ireland\","
                + "\"price\":5.99,"
                + "\"protein\":31,"
                + "\"fat\":3.6,"
                + "\"organic\":false"
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Ingredient>> response = ingredientsApi.getIngredientByName("Chicken").execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());
        assertEquals("Chicken Breast", response.body().get(0).name);
    }

    @Test
    public void getOrganic_returnsOrganicIngredients() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":2,"
                + "\"name\":\"Organic Eggs\","
                + "\"origin\":\"Ireland\","
                + "\"price\":4.20,"
                + "\"protein\":13,"
                + "\"fat\":11,"
                + "\"organic\":true"
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Ingredient>> response = ingredientsApi.getOrganic(true).execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());
        assertEquals("Organic Eggs", response.body().get(0).name);
    }

    @Test
    public void getIngredientsByMaxPrice_returnsCheapIngredients() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":3,"
                + "\"name\":\"Rice\","
                + "\"origin\":\"Spain\","
                + "\"price\":1.99,"
                + "\"protein\":2.7,"
                + "\"fat\":0.3,"
                + "\"organic\":false"
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Ingredient>> response = ingredientsApi.getIngredientsByMaxPrice(2.00).execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());
        assertEquals("Rice", response.body().get(0).name);
        assertEquals(1.99, response.body().get(0).price, 0.01);
    }
}