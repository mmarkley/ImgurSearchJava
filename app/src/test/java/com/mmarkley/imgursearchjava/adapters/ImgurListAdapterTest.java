package com.mmarkley.imgursearchjava.adapters;

import android.content.Context;

import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurDataObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import androidx.test.platform.app.InstrumentationRegistry;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ImgurListAdapterTest {
    private ImgurListAdapter adapter;
    private List<ImgurDataObject> dataObjects;
    private ImgurDataObject objectOne;
    private ImgurDataObject objectTwo;
    private ImgurDataObject objectThree;

    @Before
    public void setup() {
        dataObjects = new ArrayList<>();
        objectOne = mock(ImgurDataObject.class);
        when(objectOne.getId()).thenReturn("objectOne");
        when(objectOne.getNsfw()).thenReturn(true);
        when(objectOne.getLink()).thenReturn("https:/objectOne.jpg");

        dataObjects.add(objectOne);
        objectTwo = mock(ImgurDataObject.class);
        when(objectTwo.getId()).thenReturn("objectTwo");
        when(objectTwo.getNsfw()).thenReturn(true);
        when(objectTwo.getLink()).thenReturn("https:/objectTwo.jpg");
        dataObjects.add(objectTwo);

        objectThree = mock(ImgurDataObject.class);
        when(objectThree.getId()).thenReturn("objectThree");
        when(objectThree.getNsfw()).thenReturn(true);
        when(objectThree.getLink()).thenReturn("https:/object3.mp4");
        dataObjects.add(objectThree);


        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        adapter = new ImgurListAdapter(context, dataObjects);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void onCreateViewHolder() {
        ImgurListAdapter.ImgurListAdapterViewHolder viewHolder = adapter.onCreateViewHolder(null, 0);
        assertNotNull("Should get view holder", viewHolder);
    }

    @Test
    public void onBindViewHolder() {
        ImgurListAdapter.ImgurListAdapterViewHolder viewHolder = adapter.onCreateViewHolder(null, 0);
        adapter.onBindViewHolder(viewHolder, 1);

        assertEquals("strings should match", objectTwo.getId(), viewHolder.idTag);
        assertEquals("links should match", objectTwo.getLink(), viewHolder.dataObject.getLink());
    }

    @Test
    public void getItemCount() {
        // Test to make sure that the adapter constructed in setup has 3 elements.
        assertEquals(dataObjects.size(), adapter.getItemCount());
    }

    @Test
    public void getObjectAtIndex() {
        ImgurDataObject retrievedObject = adapter.getObjectAtIndex(0);

        assertEquals(objectOne.getId(),retrievedObject.getId());
        assertEquals(objectOne.getLink(), retrievedObject.getLink());
    }
}