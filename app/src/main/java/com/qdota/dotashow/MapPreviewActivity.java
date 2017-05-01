package com.qdota.dotashow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by zhaopan on 2016/6/16.
 */

public class MapPreviewActivity extends AppCompatActivity {

    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_map_preview);

        mImageView = (ImageView) findViewById(R.id.imageView);
        try {
            mImageView.setImageBitmap(TgaUtility.getPreview(this));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}
