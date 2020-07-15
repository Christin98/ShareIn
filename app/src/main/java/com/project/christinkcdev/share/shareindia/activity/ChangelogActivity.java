package com.project.christinkcdev.share.shareindia.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.project.christinkcdev.share.shareindia.R;
import com.project.christinkcdev.share.shareindia.app.Activity;

public class ChangelogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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