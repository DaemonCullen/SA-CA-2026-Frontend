package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class IngredientsE2ETests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private MockWebServer mockWebServer;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        RetrofitClient.setCustomBaseUrl(mockWebServer.url("/").toString());
    }

    @After
    public void tearDown() throws IOException {
        RetrofitClient.reset();
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    private void openIngredientsPage() {
        onView(withId(R.id.action_ingredients)).perform(click());
    }

    @Test
    public void ingredientsPageOpens() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openIngredientsPage();

        onView(withId(R.id.ingredientSearchView)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredientFilterSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.btnCheapIngredients)).check(matches(isDisplayed()));
    }

    @Test
    public void canClickSearchIngredients() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openIngredientsPage();

        onView(withId(R.id.ingredientSearchView))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void canOpenFilterSpinner() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openIngredientsPage();

        onView(withId(R.id.ingredientFilterSpinner)).perform(click());
        onView(withText("Organic")).check(matches(isDisplayed()));
    }

    @Test
    public void canClickCheapIngredientsFilter() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openIngredientsPage();

        onView(withId(R.id.btnCheapIngredients)).perform(click());
        onView(withText("Filter by Max Price")).check(matches(isDisplayed()));
    }

    @Test
    public void canClickHighProteinFilter() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openIngredientsPage();

        onView(withId(R.id.btnHighProtein)).perform(click());
        onView(withText("Filter by Min Protein")).check(matches(isDisplayed()));
    }

    @Test
    public void canClickLowFatFilter() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openIngredientsPage();

        onView(withId(R.id.btnLowFat)).perform(click());
        onView(withText("Filter by Max Fat")).check(matches(isDisplayed()));
    }
}
