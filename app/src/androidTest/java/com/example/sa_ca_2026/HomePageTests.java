package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomePageTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openHomePage() {
        onView(withId(R.id.home)).perform(click());
    }

    @Test
    public void loadsWelcomeText() {
        openHomePage();

        onView(withText(R.string.home_welcome))
                .check(matches(isDisplayed()));

        onView(withText(R.string.home_welcome_message))
                .check(matches(isDisplayed()));
    }

    @Test
    public void showsCards() {
        openHomePage();

        onView(withId(R.id.cardMeals))
                .check(matches(isDisplayed()));

        onView(withId(R.id.cardPlans))
                .check(matches(isDisplayed()));

        onView(withId(R.id.cardIngredients))
                .check(matches(isDisplayed()));

        onView(withId(R.id.cardSettings))
                .check(matches(isDisplayed()));
    }

    @Test
    public void mealsCardOpensMealsPage() {
        openHomePage();

        onView(withId(R.id.cardMeals))
                .perform(click());

        onView(withId(R.id.searchView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickPlansCard() {
        openHomePage();

        onView(withId(R.id.cardPlans))
                .perform(click());
    }

    @Test
    public void clickIngredientsCard() {
        openHomePage();

        onView(withId(R.id.cardIngredients))
                .perform(click());
    }

    @Test
    public void settingsCardOpensSettingsPage() {
        openHomePage();

        onView(withId(R.id.cardSettings))
                .perform(scrollTo(), click());

        onView(withId(R.id.settings))
                .check(matches(isSelected()));
    }
}