package com.mmarkley.imgursearchjava;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.mmarkley.imgursearchjava.activities.FullScreenImageViewActivity;
import com.mmarkley.imgursearchjava.fragments.SearchFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import datamodel.DataCallbackFailure;
import datamodel.DataCallbackSuccess;
import datamodel.DataModel;
import datamodel.imgurdata.ImgurDataObject;

public class MainActivity extends AppCompatActivity implements DataModel.DataModelCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FULL_SCREEN_CODE = 1001;

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

    public void launchFullScreenImageActivity(@NonNull ImgurDataObject dataObject) {
        Intent intent = new Intent(this, FullScreenImageViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("object", dataObject.getId());
        Navigation.findNavController(searchFragment.getView()).navigate(R.id.action_searchFragment_to_fullScreenImageViewActivity);
    }
}
