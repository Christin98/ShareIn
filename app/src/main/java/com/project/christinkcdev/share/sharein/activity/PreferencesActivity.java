package com.project.christinkcdev.share.sharein.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;

import com.project.christinkcdev.share.sharein.R;
import com.project.christinkcdev.share.sharein.app.Activity;
import com.project.christinkcdev.share.sharein.util.AppUtils;
import com.project.christinkcdev.share.sharein.util.PreferenceUtils;

public class PreferencesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
            onBackPressed();
        else if (id == R.id.actions_preference_main_reset_to_defaults) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.ques_resetToDefault)
                    .setMessage(R.string.text_resetPreferencesToDefaultSummary)
                    .setNegativeButton(R.string.butn_cancel, null)
                    .setPositiveButton(R.string.butn_proceed, (dialog, which) -> {

                        // TODO: This will cause two seperate sync operations to start
                        AppUtils.getDefaultPreferences(getApplicationContext()).edit()
                                .clear()
                                .apply();

                        AppUtils.getDefaultLocalPreferences(getApplicationContext()).edit()
                                .clear()
                                .apply();

                        finish();
                    })
                    .show();
        } else
            return super.onOptionsItemSelected(item);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actions_preferences_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        PreferenceUtils.syncPreferences(AppUtils.getDefaultLocalPreferences(this),
                AppUtils.getDefaultPreferences(this).getWeakManager());
    }
}