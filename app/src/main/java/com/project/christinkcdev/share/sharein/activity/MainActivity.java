package com.project.christinkcdev.share.sharein.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genonbeta.android.framework.widget.PowerfulActionMode;
import com.google.android.material.navigation.NavigationView;
import com.project.christinkcdev.share.sharein.R;
import com.project.christinkcdev.share.sharein.app.Activity;
import com.project.christinkcdev.share.sharein.config.AppConfig;
import com.project.christinkcdev.share.sharein.dialog.ShareAppDialog;
import com.project.christinkcdev.share.sharein.fragment.HomeFragment;
import com.project.christinkcdev.share.sharein.models.NetworkDevice;
import com.project.christinkcdev.share.sharein.service.CommunicationService;
import com.project.christinkcdev.share.sharein.ui.callback.PowerfulActionModeSupport;
import com.project.christinkcdev.share.sharein.util.AppUtils;
import com.project.christinkcdev.share.sharein.util.UpdateUtils;

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener, PowerfulActionModeSupport {

    public static final int REQUEST_PERMISSION_ALL = 1;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private PowerfulActionMode mActionMode;
    private HomeFragment mHomeFragment;
    private MenuItem mTrustZoneToggle;
    private IntentFilter mFilter = new IntentFilter();
    private BroadcastReceiver mReceiver = null;

    private long mExitPressTime;
    private int mChosenMenuItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.activitiy_home_fragment);
        mActionMode = findViewById(R.id.content_powerful_action_mode);
        mNavigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mTrustZoneToggle = mNavigationView.getMenu().findItem(R.id.menu_activity_trustzone);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.text_navigationDrawerOpen, R.string.text_navigationDrawerClose);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mFilter.addAction(CommunicationService.ACTION_TRUSTZONE_STATUS);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener()
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                applyAwaitingDrawerAction();
            }
        });

        mNavigationView.setNavigationItemSelectedListener(this);
        mActionMode.setOnSelectionTaskListener((started, actionMode) -> toolbar.setVisibility(!started ? View.VISIBLE : View.GONE));

        if (UpdateUtils.hasNewVersion(this))
            highlightUpdater(getDefaultPreferences().getString("availableVersion", null));

        if (!AppUtils.isLatestChangeLogSeen(this)) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.mesg_versionUpdatedChangelog)
                    .setPositiveButton(R.string.butn_yes, (dialog, which) -> {
                        AppUtils.publishLatestChangelogSeen(MainActivity.this);
                        startActivity(new Intent(MainActivity.this, ChangelogActivity.class));
                    })
                    .setNeutralButton(R.string.butn_never, (dialog, which) -> getDefaultPreferences().edit()
                            .putBoolean("show_changelog_dialog", false)
                            .apply())
                    .setNegativeButton(R.string.butn_no, (dialog, which) -> {
                        AppUtils.publishLatestChangelogSeen(MainActivity.this);
                        Toast.makeText(MainActivity.this, R.string.mesg_versionUpdatedChangelogRejected, Toast.LENGTH_SHORT).show();
                    })
                    .show();
        }

//        if (Keyword.Flavor.googlePlay.equals(AppUtils.getBuildFlavor())) {
//            MenuItem donateItem = mNavigationView.getMenu()
//                    .findItem(R.id.menu_activity_main_donate);
//
//            if (donateItem != null)
//                donateItem.setVisible(true);
//        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        createHeaderView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(mReceiver = new ActivityReceiver(), mFilter);
        requestTrustZoneStatus();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mReceiver != null)
            unregisterReceiver(mReceiver);

        mReceiver = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        mChosenMenuItemId = item.getItemId();

        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (mHomeFragment.onBackPressed())
            return;

        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else if ((System.currentTimeMillis() - mExitPressTime) < 2000)
            super.onBackPressed();
        else {
            mExitPressTime = System.currentTimeMillis();
            Toast.makeText(this, R.string.mesg_secureExit, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserProfileUpdated()
    {
        createHeaderView();
    }

    private void applyAwaitingDrawerAction()
    {
        if (mChosenMenuItemId == 0) {
            // Do nothing
        } else if (R.id.menu_activity_main_manage_devices == mChosenMenuItemId) {
            startActivity(new Intent(this, ManageDevicesActivity.class));
        } else if (R.id.menu_activity_main_about == mChosenMenuItemId) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (R.id.menu_activity_main_send_application == mChosenMenuItemId) {
            new ShareAppDialog(MainActivity.this)
                    .show();
        }
//        else if (R.id.menu_activity_main_web_share == mChosenMenuItemId) {
//            startActivity(new Intent(this, WebShareActivity.class));
//        }
        else if (R.id.menu_activity_main_preferences == mChosenMenuItemId) {
            startActivity(new Intent(this, PreferencesActivity.class));
//        } else if (R.id.menu_activity_main_exit == mChosenMenuItemId) {
//            exitApp();
//        } else if (R.id.menu_activity_main_donate == mChosenMenuItemId) {
//            try {
//                startActivity(new Intent(this, Class.forName("com.genonbeta.TrebleShot.activity.DonationActivity")));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
        } else if (R.id.menu_activity_main_dev_survey == mChosenMenuItemId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.text_developmentSurvey);
            builder.setMessage(R.string.text_developmentSurveySummary);
            builder.setNegativeButton(R.string.genfw_uwg_later, null);
            builder.setPositiveButton(R.string.butn_temp_doIt, (dialog, which) -> {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(
                            "https://docs.google.com/forms/d/e/1FAIpQLSc9S-It3mgp9Bw_SBIHXf-Kyt6-rm02SR5-jn_ipu5DfbUKjg/viewform?usp=sf_link"
                    )));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, R.string.mesg_temp_noBrowser,
                            Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (R.id.menu_activity_feedback == mChosenMenuItemId) {
            AppUtils.createFeedbackIntent(MainActivity.this);
        }
//        else if (R.id.menu_activity_trustzone == mChosenMenuItemId) {
//            toggleTrustZone();
//        }

        else if (R.id.rate_us == mChosenMenuItemId) {
            Uri uri = Uri.parse(AppConfig.URI_MARKET);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(AppConfig.URI_GOOGLE_PLAY)));
            }
        }

        mChosenMenuItemId = 0;
    }

    private void createHeaderView()
    {
        View headerView = mNavigationView.getHeaderView(0);
        MenuItem surveyItem = mNavigationView.getMenu().findItem(R.id.menu_activity_main_dev_survey);
        Configuration configuration = getApplication().getResources().getConfiguration();

        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList list = configuration.getLocales();

            if (list.size() > 0)
                for (int pos = 0; pos < list.size(); pos++)
                    if (list.get(pos).toLanguageTag().startsWith("en")) {
                        surveyItem.setVisible(true);
                        break;
                    }
        } else
            surveyItem.setVisible(configuration.locale.toString().startsWith("en"));

        if (headerView != null) {
            NetworkDevice localDevice = AppUtils.getLocalDevice(getApplicationContext());

            ImageView imageView = headerView.findViewById(R.id.layout_profile_picture_image_default);
            ImageView editImageView = headerView.findViewById(R.id.layout_profile_picture_image_preferred);
            TextView deviceNameText = headerView.findViewById(R.id.header_default_device_name_text);
            TextView versionText = headerView.findViewById(R.id.header_default_device_version_text);

            deviceNameText.setText(localDevice.nickname);
            versionText.setText(localDevice.versionName);
            loadProfilePictureInto(localDevice.nickname, imageView);

            editImageView.setOnClickListener(v -> startProfileEditor());
        }
    }

    @Override
    public PowerfulActionMode getPowerfulActionMode()
    {
        return mActionMode;
    }

    private void highlightUpdater(String availableVersion)
    {
        MenuItem item = mNavigationView.getMenu().findItem(R.id.menu_activity_main_about);
        item.setTitle(R.string.text_newVersionAvailable);
    }

    public void requestTrustZoneStatus()
    {
        AppUtils.startForegroundService(this, new Intent(this, CommunicationService.class)
                .setAction(CommunicationService.ACTION_REQUEST_TRUSTZONE_STATUS));
    }

    public void toggleTrustZone()
    {
        AppUtils.startForegroundService(this, new Intent(this, CommunicationService.class)
                .setAction(CommunicationService.ACTION_TOGGLE_SEAMLESS_MODE));
    }

    private class ActivityReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (CommunicationService.ACTION_TRUSTZONE_STATUS.equals(intent.getAction())
                    && mTrustZoneToggle != null)
                mTrustZoneToggle.setTitle(intent.getBooleanExtra(
                        CommunicationService.EXTRA_STATUS_STARTED, false)
                        ? R.string.butn_turnTrustZoneOff : R.string.butn_turnTrustZoneOn);
        }
    }

}