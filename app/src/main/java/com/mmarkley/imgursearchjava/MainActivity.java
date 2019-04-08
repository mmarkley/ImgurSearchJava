package com.mmarkley.imgursearchjava;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mmarkley.imgursearchjava.datamodel.DataCallbackFailure;
import com.mmarkley.imgursearchjava.datamodel.DataModel;
import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurDataObject;
import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurImage;
import com.mmarkley.imgursearchjava.fragments.SearchFragment;
import com.mmarkley.imgursearchjava.networking.ImgurRequest;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements DataModel.DataModelCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FULL_SCREEN_CODE = 1001;

    public static final String BUNDLE_KEY = "DataObjectId";

    SearchFragment searchFragment;
    int pageNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataModel.getInstance().init(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        DataModel.getInstance().cleanup();
        super.onDestroy();
    }

    /**
     * The MainActivity needs to know about the search activity so it can forward on the
     * information that is received in {@link MainActivity#onSuccess(List)}
     * @param searchFragment The {@link SearchFragment} to use to foward
     */
    public void setSearchFragment(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }

    /**
     * Method to call into the {@link DataModel}and retrieve the images for the specified query
     * @param query A {@link String} containing the search terms
     */
    void performSearch(String query, boolean clearData) {
        String filterType = searchFragment.getFilterType();
        if(clearData) {
            DataModel.getInstance().flushStorage();
            pageNumber = 0;
        }
        ImgurRequest request = new ImgurRequest(pageNumber++, filterType, query);
        DataModel.getInstance().fetchImages(request, this);
    }

    /**
     * method used by the SearchView in the {@link SearchFragment} to return the text that the
     * user entered
     * @param intent An {@link Intent} containing the search term
     */
    @Override
    public void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            performSearch(query, true);
        }
    }

    /**
     * Method to retrieve next page of data.
     * @param query @ {@link String} to use to query Imgur
     */
    public void nextPage(String query) {
        performSearch(query, false);

    }

    @Override
    public void onSuccess(@NonNull List<ImgurDataObject> dataObjects) {
        searchFragment.updateData(dataObjects);
    }

    @Override
    public void onFailure(@NonNull DataCallbackFailure failure) {

    }

    /**
     * Method used by the Image List to launch a new activity
     * @param dataObject The {@link ImgurDataObject} to process
     */
    public void launchFullScreenImageActivity(@NonNull ImgurDataObject dataObject) {
        Bundle bundle = new Bundle();
        Integer imageCount = dataObject.getImagesCount();
        if (null != imageCount && imageCount > 0) {
            List<ImgurImage> images = dataObject.getImages();
            if (0 < images.size()) {
                ImgurImage image = images.get(0);
                Log.i(TAG, "\tImage URL " + image.getLink());
                String link = image.getLink();
                bundle.putString(BUNDLE_KEY, link);
                View view = searchFragment.getView();
                if (null != view) {
                    Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_fullScreenImageViewActivity, bundle);
                }
            }
        }
    }
}
