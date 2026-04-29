package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class MealsE2ETest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private MockWebServer mockWebServer;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = mockWebServer.url("/").toString();
        // Redirect Retrofit to the MockWebServer
        RetrofitClient.setCustomBaseUrl(baseUrl);
    }

    @After
    public void tearDown() throws IOException {
        // Reset Retrofit to default URL and shutdown server
        RetrofitClient.reset();
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void testBottomNavigation() {
        // Meals and Plans fragments each make an API call on load — enqueue empty responses
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));

        // Check if Home is displayed initially
        onView(withText(R.string.home_welcome)).check(matches(isDisplayed()));

        // Navigate to Meals
        onView(withId(R.id.meals)).perform(click());
        onView(withId(R.id.mealsList)).check(matches(isDisplayed()));

        // Navigate to Plans
        onView(withId(R.id.action_plans)).perform(click());
        onView(withId(R.id.listViewPlans)).check(matches(isDisplayed()));

        // Navigate back to Home
        onView(withId(R.id.home)).perform(click());
        onView(withText(R.string.home_welcome)).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeCardNavigation() {
        // MealsFragment makes an API call on load — enqueue an empty response
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));

        // Click on the Meals card on the Home screen
        onView(withId(R.id.cardMeals)).perform(click());
        
        // Verify we are on the Meals page
        onView(withId(R.id.mealsList)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddMealDialogOpens() {
        // MealsFragment makes an API call on load — enqueue an empty response
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));

        // Go to Meals page
        onView(withId(R.id.meals)).perform(click());

        // Click Add Meal button
        onView(withId(R.id.btnAddMealTop)).perform(click());

        // Verify the dialog appeared by checking for its title
        onView(withText(R.string.meals_addNewMeal)).check(matches(isDisplayed()));
    }
}
