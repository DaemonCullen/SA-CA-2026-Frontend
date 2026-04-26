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

public class PlansAPITests {

    private MockWebServer mockWebServer;
    private PlansApi plansApi;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        plansApi = retrofit.create(PlansApi.class);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getAllPlans_returnsListOfPlans() throws Exception {
        String fakeJsonResponse = "["
                + "{"
                + "\"id\":1,"
                + "\"name\":\"Muscle Building Plan\","
                + "\"description\":\"High protein meals\""
                + "},"
                + "{"
                + "\"id\":2,"
                + "\"name\":\"Fat Loss Plan\","
                + "\"description\":\"Low calorie meals\""
                + "}"
                + "]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<List<Plan>> response = plansApi.getAllPlans().execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(2, response.body().size());

        assertEquals("Muscle Building Plan", response.body().get(0).name);
        assertEquals("High protein meals", response.body().get(0).description);

        assertEquals("Fat Loss Plan", response.body().get(1).name);
        assertEquals("Low calorie meals", response.body().get(1).description);
    }

    @Test
    public void createPlan_returnsCreatedPlan() throws Exception {
        String fakeJsonResponse = "{"
                + "\"id\":3,"
                + "\"name\":\"Test Plan\","
                + "\"description\":\"Test Description\""
                + "}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Plan newPlan = new Plan();
        newPlan.name = "Test Plan";
        newPlan.description = "Test Description";

        Response<Plan> response = plansApi.createPlan(newPlan).execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        assertEquals(3, response.body().id);
        assertEquals("Test Plan", response.body().name);
        assertEquals("Test Description", response.body().description);
    }

    @Test
    public void deletePlan_returnsSuccessfulResponse() throws Exception {
        String fakeJsonResponse = "{"
                + "\"id\":1,"
                + "\"name\":\"Deleted Plan\","
                + "\"description\":\"Deleted description\""
                + "}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fakeJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Response<Plan> response = plansApi.deletePlan(1).execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());

        assertEquals(1, response.body().id);
        assertEquals("Deleted Plan", response.body().name);
    }
}