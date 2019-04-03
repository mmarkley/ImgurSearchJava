package com.mmarkley.imgursearchjava.networking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import networking.ImgurRequest;

import static org.junit.Assert.assertEquals;

public class ImgurRequestTest {

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

        ImgurRequest request = new ImgurRequest(pageNumber, searchString);

        assertEquals(pageNumber, request.getPageNumber());
        assertEquals(searchString, request.getSearchTerm());
    }

    @Test
    public void testUri() {
        String searchString = "empty search";
        long pageNumber = 5;

        ImgurRequest request = new ImgurRequest(pageNumber, searchString);

        assertEquals("", request.getUrl());
    }
}