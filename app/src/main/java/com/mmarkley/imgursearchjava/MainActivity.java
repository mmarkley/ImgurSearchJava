package com.mmarkley.imgursearchjava;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import datamodel.DataCallbackFailure;
import datamodel.DataCallbackSuccess;
import datamodel.DataModel;
import datamodel.imgurdata.ImgurDataObject;
import datamodel.imgurdata.ImgurResponse;

public class MainActivity extends AppCompatActivity implements DataModel.DataModelCallback {

    SearchView searchEditView;
    ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataModel.getInstance().init(this);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchEditView = findViewById(R.id.main_search_view);
        // Assumes current activity is the searchable activity
        searchEditView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchEditView.setIconifiedByDefault(false);
        mainListView = findViewById(R.id.main_search_results_view);
    }

    @Override
    public void onDestroy() {
        DataModel.getInstance().cleanup();
        super.onDestroy();
    }

    @Override
    public void onSuccess(@NonNull DataCallbackSuccess success) {
        ImgurResponse dataObject = success.getImgurResponse();
        List<ImgurDataObject> imageObjects = dataObject.getData();
        List<String> titles = new ArrayList<>();
        for(int i = 0; i < imageObjects.size(); i++) {
            ImgurDataObject obj = imageObjects.get(i);
            titles.add(obj.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, titles);
        mainListView.setAdapter(adapter);
    }

    @Override
    public void onFailure(@NonNull DataCallbackFailure failure) {

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
}
