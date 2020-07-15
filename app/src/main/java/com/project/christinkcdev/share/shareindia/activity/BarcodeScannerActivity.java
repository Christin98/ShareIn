package com.project.christinkcdev.share.shareindia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.christinkcdev.share.shareindia.R;

public class BarcodeScannerActivity extends AppCompatActivity {

    public static final String EXTRA_DEVICE_ID = "extraDeviceId";
    public static final String EXTRA_CONNECTION_ADAPTER = "extraConnectionAdapter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
    }
}