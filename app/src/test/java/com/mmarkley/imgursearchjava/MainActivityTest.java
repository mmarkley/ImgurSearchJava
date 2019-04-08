package com.mmarkley.imgursearchjava;

import android.app.SearchManager;
import android.content.Intent;

import com.mmarkley.imgursearchjava.fragments.SearchFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private static final String SEARCH_STRING = "any banana";

    @Before
    public void setUp() {

    }

    @Test
    public void testOnNewIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, SEARCH_STRING);
        MainActivity spy = spy(new MainActivity());
        SearchFragment fragment = mock(SearchFragment.class);
        when(fragment.getFilterType()).thenReturn("viral");
        spy.setSearchFragment(fragment);

        spy.onNewIntent(intent);
        verify(spy).performSearch(SEARCH_STRING, true);
    }
}
