package com.project.christinkcdev.share.shareindia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.project.christinkcdev.share.shareindia.R;
import com.project.christinkcdev.share.shareindia.app.Activity;

public class ManageDevicesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_devices);

        findViewById(R.id.fixConnectionButton).setOnClickListener(v -> startActivity(new Intent(ManageDevicesActivity.this, ConnectionManagerActivity.class)
                .putExtra(ConnectionManagerActivity.EXTRA_REQUEST_TYPE, ConnectionManagerActivity.RequestType.RETURN_RESULT.toString())
                .putExtra(ConnectionManagerActivity.EXTRA_ACTIVITY_SUBTITLE, getString(R.string.text_fixConnection))));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}