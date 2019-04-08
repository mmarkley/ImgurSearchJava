package com.mmarkley.imgursearchjava;

import com.mmarkley.imgursearchjava.screens.SearchScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        MainActivity activity = activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void smokeTest() {
        SearchScreen searchScreen = new SearchScreen();
    }


}