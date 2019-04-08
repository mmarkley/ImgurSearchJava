package com.mmarkley.imgursearchjava.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.mmarkley.imgursearchjava.MainActivity;
import com.mmarkley.imgursearchjava.R;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullScreenImageViewActivity extends AppCompatActivity {
    private static final String TAG = FullScreenImageViewActivity.class.getSimpleName();

    private ImageView imageView;
    private String imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_image_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        imageView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually close this activity
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if (null != extras) {
                imageLink = extras.getString(MainActivity.BUNDLE_KEY);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != imageLink) {
            if (null != imageView) {
                Log.i(TAG, "loading " + imageLink);
                Picasso.get().load(imageLink).placeholder(R.drawable.loading).fit().centerCrop(Gravity.HORIZONTAL_GRAVITY_MASK).into(imageView);
            }
        }
    }

}
