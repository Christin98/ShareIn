package com.project.christinkcdev.share.sharein.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.genonbeta.android.framework.widget.PowerfulActionMode
import com.google.android.material.navigation.NavigationView
import com.project.christinkcdev.share.sharein.R
import com.project.christinkcdev.share.sharein.app.Activity

class MainActivity : Activity(), NavigationView.OnNavigationItemSelectedListener,
    PowerfulActionModeSupport {
    private var mNavigationView: NavigationView? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mActionMode: PowerfulActionMode? = null
    private var mHomeFragment: HomeFragment? = null
    private val mTrustZoneToggle: MenuItem? = null
    private val mFilter: IntentFilter = IntentFilter()
    private var mReceiver: BroadcastReceiver? = null
    private var mExitPressTime: Long = 0
    private var mChosenMenuItemId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mHomeFragment =
            supportFragmentManager.findFragmentById(R.id.activitiy_home_fragment) as HomeFragment?
        mActionMode = findViewById(R.id.content_powerful_action_mode)
        mNavigationView = findViewById<NavigationView>(R.id.nav_view)
        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        //        mTrustZoneToggle = mNavigationView.getMenu().findItem(R.id.menu_activity_trustzone);
        val toggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            toolbar,
            R.string.text_navigationDrawerOpen,
            R.string.text_navigationDrawerClose
        )
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mFilter.addAction(CommunicationService.ACTION_TRUSTZONE_STATUS)
        mDrawerLayout.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                applyAwaitingDrawerAction()
            }
        })
        mNavigationView.setNavigationItemSelectedListener(this)
        mActionMode.setOnSelectionTaskListener { started, actionMode ->
            toolbar.visibility = if (!started) View.VISIBLE else View.GONE
        }
        if (Updater.hasNewVersion(this)) highlightUpdater(
            defaultPreferences.getString(
                "availableVersion",
                null
            )
        )
        if (!AppUtils.isLatestChangeLogSeen(this)) {
            AlertDialog.Builder(this)
                .setMessage(R.string.mesg_versionUpdatedChangelog)
                .setPositiveButton(
                    R.string.butn_yes,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        AppUtils.publishLatestChangelogSeen(this@MainActivity)
                        startActivity(Intent(this@MainActivity, ChangelogActivity::class.java))
                    })
                .setNeutralButton(
                    R.string.butn_never,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        defaultPreferences.edit()
                            .putBoolean("show_changelog_dialog", false)
                            .apply()
                    })
                .setNegativeButton(
                    R.string.butn_no,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        AppUtils.publishLatestChangelogSeen(this@MainActivity)
                        Toast.makeText(
                            this@MainActivity,
                            R.string.mesg_versionUpdatedChangelogRejected,
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                .show()
        }

//        if (Keyword.Flavor.googlePlay.equals(AppUtils.getBuildFlavor())) {
//            MenuItem donateItem = mNavigationView.getMenu()
//                    .findItem(R.id.menu_activity_main_donate);
//
//            if (donateItem != null)
//                donateItem.setVisible(true);
//        }
    }

    override fun onStart() {
        super.onStart()
        createHeaderView()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(ActivityReceiver().also { mReceiver = it }, mFilter)
        requestTrustZoneStatus()
    }

    override fun onPause() {
        super.onPause()
        if (mReceiver != null) unregisterReceiver(mReceiver)
        mReceiver = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mChosenMenuItemId = item.itemId
        if (mDrawerLayout != null) mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (mHomeFragment.onBackPressed()) return
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawer(
            GravityCompat.START
        ) else if (System.currentTimeMillis() - mExitPressTime < 2000) super.onBackPressed() else {
            mExitPressTime = System.currentTimeMillis()
            Toast.makeText(this, R.string.mesg_secureExit, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onUserProfileUpdated() {
        createHeaderView()
    }

    private fun applyAwaitingDrawerAction() {
        if (mChosenMenuItemId == 0) {
            // Do nothing
        } else if (R.id.menu_activity_main_manage_devices == mChosenMenuItemId) {
            startActivity(Intent(this, ManageDevicesActivity::class.java))
        } else if (R.id.menu_activity_main_about == mChosenMenuItemId) {
            startActivity(Intent(this, AboutActivity::class.java))
        } else if (R.id.menu_activity_main_send_application == mChosenMenuItemId) {
            ShareAppDialog(this@MainActivity)
                .show()
        } else if (R.id.menu_activity_main_preferences == mChosenMenuItemId) {
            startActivity(Intent(this, PreferencesActivity::class.java))
            //        } else if (R.id.menu_activity_main_exit == mChosenMenuItemId) {
//            exitApp();
//        } else if (R.id.menu_activity_main_donate == mChosenMenuItemId) {
//            try {
//                startActivity(new Intent(this, Class.forName("com.genonbeta.TrebleShot.activity.DonationActivity")));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
        } else if (R.id.menu_activity_main_dev_survey == mChosenMenuItemId) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.text_developmentSurvey)
            builder.setMessage(R.string.text_developmentSurveySummary)
            builder.setNegativeButton(R.string.genfw_uwg_later, null)
            builder.setPositiveButton(
                R.string.butn_temp_doIt,
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    try {
                        startActivity(
                            Intent(Intent.ACTION_VIEW).setData(
                                Uri.parse(
                                    "https://docs.google.com/forms/d/e/1FAIpQLSc9S-It3mgp9Bw_SBIHXf-Kyt6-rm02SR5-jn_ipu5DfbUKjg/viewform?usp=sf_link"
                                )
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            this@MainActivity, R.string.mesg_temp_noBrowser,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            builder.show()
        } else if (R.id.menu_activity_feedback == mChosenMenuItemId) {
            AppUtils.createFeedbackIntent(this@MainActivity)
        } else if (R.id.rate_us == mChosenMenuItemId) {
            val uri = Uri.parse(AppConfig.URI_MARKET)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(AppConfig.URI_GOOGLE_PLAY)
                    )
                )
            }
        }
        mChosenMenuItemId = 0
    }

    private fun createHeaderView() {
        val headerView: View = mNavigationView.getHeaderView(0)
        val surveyItem: MenuItem =
            mNavigationView.getMenu().findItem(R.id.menu_activity_main_dev_survey)
        val configuration = application.resources.configuration
        if (Build.VERSION.SDK_INT >= 24) {
            val list: LocaleList = configuration.locales
            if (list.size() > 0) for (pos in 0 until list.size()) if (list.get(pos).toLanguageTag()
                    .startsWith("en")
            ) {
                surveyItem.isVisible = true
                break
            }
        } else surveyItem.isVisible = configuration.locale.toString().startsWith("en")
        if (headerView != null) {
            val localDevice: NetworkDevice = AppUtils.getLocalDevice(applicationContext)
            val imageView =
                headerView.findViewById<ImageView>(R.id.layout_profile_picture_image_default)
            val editImageView =
                headerView.findViewById<ImageView>(R.id.layout_profile_picture_image_preferred)
            val deviceNameText: TextView =
                headerView.findViewById<TextView>(R.id.header_default_device_name_text)
            val versionText: TextView =
                headerView.findViewById<TextView>(R.id.header_default_device_version_text)
            deviceNameText.setText(localDevice.nickname)
            versionText.setText(localDevice.versionName)
            loadProfilePictureInto(localDevice.nickname, imageView)
            editImageView.setOnClickListener { v: View? -> startProfileEditor() }
        }
    }

    val powerfulActionMode: PowerfulActionMode
        get() = mActionMode

    private fun highlightUpdater(availableVersion: String?) {
        val item: MenuItem = mNavigationView.getMenu().findItem(R.id.menu_activity_main_about)
        item.setTitle(R.string.text_newVersionAvailable)
    }

    fun requestTrustZoneStatus() {
        AppUtils.startForegroundService(
            this, Intent(this, CommunicationService::class.java)
                .setAction(CommunicationService.ACTION_REQUEST_TRUSTZONE_STATUS)
        )
    }

    fun toggleTrustZone() {
        AppUtils.startForegroundService(
            this, Intent(this, CommunicationService::class.java)
                .setAction(CommunicationService.ACTION_TOGGLE_SEAMLESS_MODE)
        )
    }

    private inner class ActivityReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (CommunicationService.ACTION_TRUSTZONE_STATUS == intent.getAction() && mTrustZoneToggle != null) mTrustZoneToggle.setTitle(
                if (intent.getBooleanExtra(
                        CommunicationService.EXTRA_STATUS_STARTED, false
                    )
                ) R.string.butn_turnTrustZoneOff else R.string.butn_turnTrustZoneOn
            )
        }
    }

    companion object {
        const val REQUEST_PERMISSION_ALL = 1
    }
}