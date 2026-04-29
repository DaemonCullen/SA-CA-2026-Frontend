package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

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
public class PlansE2ETests {

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

    private void openPlansPage() {
        onView(withId(R.id.action_plans)).perform(click());
    }

    @Test
    public void plansPageOpens() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openPlansPage();

        onView(withText(R.string.plans_page_heading))
                .check(matches(isDisplayed()));

        onView(withId(R.id.bannerInputGoals))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnCreatePlan))
                .check(matches(isDisplayed()));

        onView(withId(R.id.listViewPlans))
                .check(matches(isDisplayed()));
    }

    @Test
    public void goalsBannerCanBeClicked() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openPlansPage();

        onView(withId(R.id.bannerInputGoals))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void createPlanDialogOpens() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        openPlansPage();

        onView(withId(R.id.btnCreatePlan))
                .perform(click());

        onView(withText(R.string.plans_page_create_plan))
                .check(matches(isDisplayed()));

        onView(withText(R.string.plans_page_create))
                .check(matches(isDisplayed()));

        onView(withText(R.string.plans_page_cancel))
                .check(matches(isDisplayed()));
    }
}
