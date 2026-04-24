package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.action.ViewActions.replaceText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PlansPageTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openPlansPage() {
        onView(withId(R.id.action_plans)).perform(click());
    }

    @Test
    public void navigationOpensPlansPage() {
        openPlansPage();

        onView(withText("Meal Plans"))
                .check(matches(isDisplayed()));

        onView(withText("Create 7-day plans tailored to your goals"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void mainViewsVisible() {
        openPlansPage();

        onView(withId(R.id.bannerInputGoals))
                .check(matches(isDisplayed()));

        onView(withId(R.id.textViewPlansTitle))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnCreatePlan))
                .check(matches(isDisplayed()));

        onView(withId(R.id.listViewPlans))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickGoalsBanner() {
        openPlansPage();

        onView(withId(R.id.bannerInputGoals))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
    }

    @Test
    public void canClickCreatePlan() {
        openPlansPage();

        onView(withId(R.id.btnCreatePlan))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
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

    @Test
    public void createPlanDialogShowsInputs() {
        openPlansPage();

        onView(withId(R.id.btnCreatePlan))
                .perform(click());

        onView(withHint(R.string.plans_page_planName))
                .check(matches(isDisplayed()));

        onView(withHint(R.string.plans_page_description))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canTypeIntoCreatePlanDialog() {
        openPlansPage();

        onView(withId(R.id.btnCreatePlan))
                .perform(click());

        onView(withHint(R.string.plans_page_planName))
                .perform(replaceText("Test Plan"), closeSoftKeyboard());

        onView(withHint(R.string.plans_page_description))
                .perform(replaceText("Test Description"), closeSoftKeyboard());

        onView(withText("Test Plan"))
                .check(matches(isDisplayed()));

        onView(withText("Test Description"))
                .check(matches(isDisplayed()));
    }
}