package com.example.javerill.wailpay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by javerill on 9/30/2016.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        // Set Collapsing Toolbar layout to the screen
//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        // Set title of Detail page
//        collapsingToolbar.setTitle(getString(R.string.app_name));
    }

}
