package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IngredientsE2ETests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openIngredientsPage() {
        onView(withId(R.id.action_ingredients)).perform(click());
    }

    @Test
    public void ingredientsPageOpens() {
        openIngredientsPage();

        onView(withId(R.id.ingredientSearchView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.ingredientFilterSpinner))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnCheapIngredients))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickSearchIngredients() {
        openIngredientsPage();

        onView(withId(R.id.ingredientSearchView))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void canOpenFilterSpinner() {
        openIngredientsPage();

        onView(withId(R.id.ingredientFilterSpinner))
                .perform(click());

        onView(withText("Organic"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickCheapIngredientsFilter() {
        openIngredientsPage();

        onView(withId(R.id.btnCheapIngredients))
                .perform(click());

        onView(withText("Filter by Max Price"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickHighProteinFilter() {
        openIngredientsPage();

        onView(withId(R.id.btnHighProtein))
                .perform(click());

        onView(withText("Filter by Min Protein"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickLowFatFilter() {
        openIngredientsPage();

        onView(withId(R.id.btnLowFat))
                .perform(click());

        onView(withText("Filter by Max Fat"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void ingredientsLoadFromApi() {
        openIngredientsPage();

        onView(withId(R.id.ingredientsListView))
                .check(matches(isDisplayed()));

        onView(withText("Chicken - Ireland ($5.0)"))
                .check(matches(isDisplayed()));
    }
}