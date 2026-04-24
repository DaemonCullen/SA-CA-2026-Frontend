package com.example.sa_ca_2026;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SettingsPageTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void openSettingsPage() {
        onView(withId(R.id.settings)).perform(click());
    }

    @Test
    public void navigationOpensSettingsPage() {
        openSettingsPage();

        onView(withId(R.id.dark_mode_item))
                .check(matches(isDisplayed()));

        onView(withId(R.id.language_item))
                .check(matches(isDisplayed()));
    }

    @Test
    public void darkModeVisible() {
        openSettingsPage();

        onView(withId(R.id.dark_mode_item))
                .check(matches(isDisplayed()));

        onView(withId(R.id.dark_mode_switch))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canClickDarkMode() {
        openSettingsPage();

        onView(withId(R.id.dark_mode_item))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
    }

    @Test
    public void canClickLanguage() {
        openSettingsPage();

        onView(withId(R.id.language_item))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
    }

    @Test
    public void languageDialogOpens() {
        openSettingsPage();

        onView(withId(R.id.language_item))
                .perform(click());

        onView(withText("Choose Language"))
                .check(matches(isDisplayed()));

        onView(withText("English"))
                .check(matches(isDisplayed()));

        onView(withText("Gaeilge"))
                .check(matches(isDisplayed()));

        onView(withText("Español"))
                .check(matches(isDisplayed()));
    }
}