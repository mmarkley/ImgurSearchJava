package com.mmarkley.imgursearchjava.screens;

import com.mmarkley.imgursearchjava.R;

import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SearchScreen {

    public SearchScreen() {
        ViewInteraction searchItem = onView(withId(R.id.main_search_view));
        searchItem.check(matches(isDisplayed()));
    }
}
