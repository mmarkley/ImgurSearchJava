package com.mmarkley.imgursearchjava.networking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ImgurRequestTest {
    private static final String EXPECTED_URI = "https://api.imgur.com/3/gallery/search/viral/5?q=empty%20search&mature=false";
    @Before
    public void initTest() {

    }

    @After
    public void teardownTest() {

    }

    @Test
    public void testSingleArgumentConstructor() {
        String searchString = "empty search";
        ImgurRequest request = new ImgurRequest(searchString);

        assertEquals(0, request.getPageNumber());
        assertEquals(searchString, request.getSearchTerm());
    }

    @Test
    public void testTwoArgumentConstructor() {
        String searchString = "empty search";
        long pageNumber = 10;

        ImgurRequest request = new ImgurRequest(pageNumber, "viral", searchString);

        assertEquals(pageNumber, request.getPageNumber());
        assertEquals(searchString, request.getSearchTerm());
    }

    @Test
    public void testUri() {
        String searchString = "empty search";
        long pageNumber = 5;

        ImgurRequest request = new ImgurRequest(pageNumber, "viral", searchString);

        assertEquals("URI should match", EXPECTED_URI, request.getUrl());
    }
}