package com.mmarkley.imgursearchjava;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import datamodel.DataCallbackFailure;
import datamodel.DataCallbackSuccess;
import datamodel.DataModel;

public class MainActivity extends AppCompatActivity implements DataModel.DataModelCallback {

    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.main_search_string);
    }

    @Override
    public void onSuccess(@NonNull DataCallbackSuccess success) {

    }

    @Override
    public void onFailure(@NonNull DataCallbackFailure failure) {

    }
}
