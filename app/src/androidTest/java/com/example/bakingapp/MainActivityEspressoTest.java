package com.example.bakingapp;

import com.example.bakingapp.UI.MainActivity;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityScenarioRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recipesRecyclerView() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(click())
                .check(matches(isDisplayed()));
    }
    private IdlingResource mIdlingResource;

//    @Before
//    public void registerIdlingResource() {
//        mIdlingResource = activityScenarioRule.getActivity().getIdlingResource();
//        Espresso.registerIdlingResources(mIdlingResource);
//    }

    @Test
    public void idlingResourceTest() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
