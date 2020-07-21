package com.project.christinkcdev.share.sharein.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.project.christinkcdev.share.sharein.R;
import com.project.christinkcdev.share.sharein.app.Activity;

public class ThirdPartyLibrariesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_libraries);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
            onBackPressed();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}