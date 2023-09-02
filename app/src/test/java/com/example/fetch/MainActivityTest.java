package com.example.fetch;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recyclerViewIsDisplayed() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void testButtonsAreDisplayed() {
        onView(withId(R.id.button_listId_1)).check(matches(isDisplayed()));
        onView(withId(R.id.button_listId_2)).check(matches(isDisplayed()));
        onView(withId(R.id.button_listId_3)).check(matches(isDisplayed()));
        onView(withId(R.id.button_listId_4)).check(matches(isDisplayed()));
    }

    @Test
    public void testButtonContentDescription() {
        onView(withContentDescription("Show items for List ID 1")).check(matches(isDisplayed()));
        onView(withContentDescription("Show items for List ID 2")).check(matches(isDisplayed()));
        onView(withContentDescription("Show items for List ID 3")).check(matches(isDisplayed()));
        onView(withContentDescription("Show items for List ID 4")).check(matches(isDisplayed()));
    }

    @Test
    public void testListIdButtonFunctionality() {
        // Assuming the initial state is displaying items for List ID 1
        onView(withId(R.id.button_listId_2)).perform(click());
        // Add logic to validate that the data now shown is from List ID 2
    }
}
