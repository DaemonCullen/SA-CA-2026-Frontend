package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PlansE2ETests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openPlansPage() {
        onView(withId(R.id.action_plans)).perform(click());
    }

    @Test
    public void plansPageOpens() {
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
        openPlansPage();

        onView(withId(R.id.bannerInputGoals))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void createPlanDialogOpens() {
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