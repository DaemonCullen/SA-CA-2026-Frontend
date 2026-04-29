package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class MealsE2ETest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testBottomNavigation() {
        // Check if Home is displayed initially (Using string resource to avoid mismatch)
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
        // Click on the Meals card on the Home screen
        onView(withId(R.id.cardMeals)).perform(click());
        
        // Verify we are on the Meals page
        onView(withId(R.id.mealsList)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddMealDialogOpens() {
        // Go to Meals page
        onView(withId(R.id.meals)).perform(click());

        // Click Add Meal button
        onView(withId(R.id.btnAddMealTop)).perform(click());

        // Verify the dialog appeared by checking for its title
        onView(withText(R.string.meals_addNewMeal)).check(matches(isDisplayed()));
    }
}
