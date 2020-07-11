package com.project.christinkcdev.share.shareindia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.christinkcdev.share.shareindia.R;

public class ShareActivity extends AppCompatActivity {

    public static final String TAG = "ShareActivity";

    public static final String ACTION_SEND = "genonbeta.intent.action.TREBLESHOT_SEND";
    public static final String ACTION_SEND_MULTIPLE = "genonbeta.intent.action.TREBLESHOT_SEND_MULTIPLE";

    public static final String EXTRA_FILENAME_LIST = "extraFileNames";
    public static final String EXTRA_DEVICE_ID = "extraDeviceId";
    public static final String EXTRA_GROUP_ID = "extraGroupId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
    }
}