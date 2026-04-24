package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
public class MealsPageTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openMealsPage() {
        onView(withId(R.id.meals)).perform(click());
    }

    @Test
    public void navigationOpensMealsPage() {
        openMealsPage();

        onView(withId(R.id.searchView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.sortSpinner))
                .check(matches(isDisplayed()));

        onView(withId(R.id.filterButton))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnAddMealTop))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canTypeInSearchBar() {
        openMealsPage();

        onView(withId(R.id.searchView))
                .perform(click());

        onView(withId(androidx.appcompat.R.id.search_src_text))
                .perform(clearText(), typeText("chicken"), closeSoftKeyboard());

        onView(withId(androidx.appcompat.R.id.search_src_text))
                .check(matches(withText("chicken")));
    }

    @Test
    public void addNewMealButtonOpensCreateMealPage() {
        openMealsPage();

        onView(withId(R.id.btnAddMealTop))
                .perform(click());

        onView(withText(R.string.meals_addNewMeal))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickFilterButton() {
        openMealsPage();

        onView(withId(R.id.filterButton))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void canClickSort() {
        openMealsPage();

        onView(withId(R.id.sortSpinner))
                .check(matches(isDisplayed()))
                .perform(click());
    }
}