package com.project.christinkcdev.share.shareindia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.christinkcdev.share.shareindia.R;

public class WebShareActivity extends AppCompatActivity {

    public static final String EXTRA_WEBSERVER_START_REQUIRED = "extraStartRequired";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_share);
    }
}