package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IngredientsPageTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openIngredientsPage() {
        onView(withId(R.id.action_ingredients)).perform(click());
    }

    @Test
    public void navigationOpensIngredientsPage() {
        openIngredientsPage();

        onView(withText("All Ingredients"))
                .check(matches(isDisplayed()));

        onView(withText("Search and filter ingredients"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void mainViewsAreVisible() {
        openIngredientsPage();

        onView(withId(R.id.ingredientSearchView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.ingredientFilterSpinner))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnCheapIngredients))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnHighProtein))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnLowFat))
                .check(matches(isDisplayed()));

        onView(withId(R.id.ingredientsListView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickFilters() {
        openIngredientsPage();

        onView(withId(R.id.btnCheapIngredients))
                .check(matches(isClickable()));

        onView(withId(R.id.btnHighProtein))
                .check(matches(isClickable()));

        onView(withId(R.id.btnLowFat))
                .check(matches(isClickable()));
    }

    @Test
    public void cheapFilterDialogOpens() {
        openIngredientsPage();

        onView(withId(R.id.btnCheapIngredients))
                .perform(click());

        onView(withText("Filter by Max Price"))
                .check(matches(isDisplayed()));

        onView(withText("Filter"))
                .check(matches(isDisplayed()));

        onView(withText("Cancel"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void highProteinFilterDialogOpens() {
        openIngredientsPage();

        onView(withId(R.id.btnHighProtein))
                .perform(click());

        onView(withId(R.id.editFilterNumber))
                .check(matches(isDisplayed()));

        onView(withText("Filter"))
                .check(matches(isDisplayed()));

        onView(withText("Cancel"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void lowFatFilterDialogOpens() {
        openIngredientsPage();

        onView(withId(R.id.btnLowFat))
                .perform(click());

        onView(withText("Filter by Max Fat"))
                .check(matches(isDisplayed()));

        onView(withText("Filter"))
                .check(matches(isDisplayed()));

        onView(withText("Cancel"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canTypeInCheapFilterDialog() {
        openIngredientsPage();

        onView(withId(R.id.btnCheapIngredients))
                .perform(click());

        onView(withId(R.id.editFilterNumber))
                .perform(replaceText("20"), closeSoftKeyboard());

        onView(withText("20"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickIngredientFilterSpinner() {
        openIngredientsPage();

        onView(withId(R.id.ingredientFilterSpinner))
                .perform(click());

        onData(anything())
                .inRoot(isPlatformPopup())
                .atPosition(0)
                .check(matches(isDisplayed()));
    }
}