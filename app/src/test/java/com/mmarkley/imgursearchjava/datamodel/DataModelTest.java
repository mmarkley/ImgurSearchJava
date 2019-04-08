package com.mmarkley.imgursearchjava.datamodel;

import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurDataObject;
import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurImage;
import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DataModelTest {

    private List<ImgurDataObject> dataObjects;

    @Before
    public void setup() {
        dataObjects = new ArrayList<>();
        ImgurDataObject objectOne = mock(ImgurDataObject.class);
        when(objectOne.getId()).thenReturn("objectOne");
        when(objectOne.getNsfw()).thenReturn(false);
        List<ImgurImage> imageListOne = new ArrayList<>();
        when(objectOne.getImagesCount()).thenReturn(1);
        when(objectOne.getImages()).thenReturn(imageListOne);
        ImgurImage imageOne = mock(ImgurImage.class);
        when(imageOne.getLink()).thenReturn("https://objectOne.jpg");
        imageListOne.add(imageOne);

        dataObjects.add(objectOne);
        ImgurDataObject objectTwo = mock(ImgurDataObject.class);
        when(objectTwo.getId()).thenReturn("objectTwo");
        when(objectTwo.getNsfw()).thenReturn(false);
        List<ImgurImage> imageListTwo = new ArrayList<>();
        when(objectTwo.getImagesCount()).thenReturn(1);
        when(objectTwo.getImages()).thenReturn(imageListTwo);
        ImgurImage imageTwo = mock(ImgurImage.class);

        when(imageTwo.getLink()).thenReturn("https:/objectTwo.jpg");
        imageListTwo.add(imageTwo);
        dataObjects.add(objectTwo);

        ImgurDataObject objectThree = mock(ImgurDataObject.class);
        when(objectThree.getId()).thenReturn("objectThree");
        when(objectThree.getNsfw()).thenReturn(true);
        List<ImgurImage> imageListThree = new ArrayList<>();
        when(objectThree.getImagesCount()).thenReturn(1);
        when(objectThree.getImages()).thenReturn(imageListThree);
        ImgurImage imageThree = mock(ImgurImage.class);
        when(imageThree.getLink()).thenReturn("https://objectThree.mp4");
        imageListThree.add(imageTwo);
        dataObjects.add(objectThree);
    }

    @After
    public void teardown() {
        // Clear everything from the internal storage
        DataModel.getInstance().flushStorage();
    }

    @Test
    public void testFilterResponse() {
        ImgurResponse response = new ImgurResponse();
        response.setData(dataObjects);

        List<ImgurDataObject> filteredResponse = DataModel.getInstance().filterResponse(response);
        assertNotNull("Should get a list back", filteredResponse);
        assertEquals("Should be two elements in the list", 2, filteredResponse.size());
    }

    @Test
    public void testGetCurrentObjects() {
        ImgurResponse response = new ImgurResponse();
        response.setData(dataObjects);

        List<ImgurDataObject> filteredResponse = DataModel.getInstance().filterResponse(response);
        assertEquals("Should be two elements in the list", 2, filteredResponse.size());
        List<ImgurDataObject> currentObjects = DataModel.getInstance().getCurrentObjects();
        assertEquals("Should be two elements in the list", 2, currentObjects.size());
    }

}