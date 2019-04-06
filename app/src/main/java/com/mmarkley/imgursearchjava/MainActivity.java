package com.mmarkley.imgursearchjava;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mmarkley.imgursearchjava.fragments.SearchFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import datamodel.DataCallbackFailure;
import datamodel.DataCallbackSuccess;
import datamodel.DataModel;
import datamodel.imgurdata.ImgurDataObject;
import datamodel.imgurdata.ImgurImage;

public class MainActivity extends AppCompatActivity implements DataModel.DataModelCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FULL_SCREEN_CODE = 1001;

    public static final String BUNDLE_KEY = "DataObjectId";

    SearchFragment searchFragment;


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

    public void setSearchFragment(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }

    private void performSearch(String query) {
        DataModel.getInstance().fetchImages(query, this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            performSearch(query);
        }
    }

    @Override
    public void onSuccess(@NonNull DataCallbackSuccess success) {
        searchFragment.updateData(success);
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
