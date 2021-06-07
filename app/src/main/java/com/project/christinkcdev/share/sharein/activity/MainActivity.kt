package com.project.christinkcdev.share.sharein.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.project.christinkcdev.share.sharein.R
import com.project.christinkcdev.share.sharein.app.Activity
import com.project.christinkcdev.share.sharein.databinding.LayoutUserProfileBinding
import com.project.christinkcdev.share.sharein.dialog.ShareAppDialog
import com.project.christinkcdev.share.sharein.util.Graphics
import com.project.christinkcdev.share.sharein.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : Activity(), NavigationView.OnNavigationItemSelectedListener {

    private val userProfileViewModel: UserProfileViewModel by viewModels()

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            Graphics.saveClientPictureLocal(applicationContext, uri)
        }
    }

    private val userProfileBinding by lazy {
        LayoutUserProfileBinding.bind(navigationView.getHeaderView(0)).also {
            it.viewModel = userProfileViewModel
            it.lifecycleOwner = this
            it.editProfileClickListener = View.OnClickListener {

            }
        }
    }

    private lateinit var navigationView: NavigationView

    private lateinit var drawerLayout: DrawerLayout

    private var pendingMenuItemId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        //        mTrustZoneToggle = mNavigationView.getMenu().findItem(R.id.menu_activity_trustzone);
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.text_navigationDrawerOpen,
            R.string.text_navigationDrawerClose
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
//        mFilter.addAction(CommunicationService.ACTION_TRUSTZONE_STATUS)
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                applyAwaitingDrawerAction()
            }
        })
        navigationView.setNavigationItemSelectedListener(this)
//        mActionMode.setOnSelectionTaskListener { started, actionMode ->
//            toolbar.visibility = if (!started) View.VISIBLE else View.GONE
//        }
        if (updater.hasNewVersion()) {
            highlightUpdater()
        }
//        if (!AppUtils.isLatestChangeLogSeen(this)) {
//            AlertDialog.Builder(this)
//                .setMessage(R.string.mesg_versionUpdatedChangelog)
//                .setPositiveButton(
//                    R.string.butn_yes,
//                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
//                        AppUtils.publishLatestChangelogSeen(this@MainActivity)
//                        startActivity(Intent(this@MainActivity, ChangelogActivity::class.java))
//                    })
//                .setNeutralButton(
//                    R.string.butn_never,
//                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
//                        defaultPreferences.edit()
//                            .putBoolean("show_changelog_dialog", false)
//                            .apply()
//                    })
//                .setNegativeButton(
//                    R.string.butn_no,
//                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
//                        AppUtils.publishLatestChangelogSeen(this@MainActivity)
//                        Toast.makeText(
//                            this@MainActivity,
//                            R.string.mesg_versionUpdatedChangelogRejected,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    })
//                .show()
//        }

//        if (Keyword.Flavor.googlePlay.equals(AppUtils.getBuildFlavor())) {
//            MenuItem donateItem = mNavigationView.getMenu()
//                    .findItem(R.id.menu_activity_main_donate);
//
//            if (donateItem != null)
//                donateItem.setVisible(true);
//        }

        findViewById<View>(R.id.sendLayoutButton).setOnClickListener {
            startActivity(Intent(it.context, ContentSharingActivity::class.java))
        }
        findViewById<View>(R.id.receiveLayoutButton).setOnClickListener {
//            startActivity(Intent(it.context, ReceiveActivity::class.java))
        }

        userProfileBinding.executePendingBindings()
    }

//    override fun onStart() {
//        super.onStart()
//        createHeaderView()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        registerReceiver(ActivityReceiver().also { mReceiver = it }, mFilter)
//        requestTrustZoneStatus()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (mReceiver != null) unregisterReceiver(mReceiver)
//        mReceiver = null
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actions_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actions_home_transfer_history) {
//            startActivity(Intent(this, SharedTextActivity::class.java))
        } else {
            return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        pendingMenuItemId  = item.itemId
//        if (drawerLayout != null)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

//    override fun onBackPressed() {
//        if (mHomeFragment.onBackPressed()) return
//        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawer(
//            GravityCompat.START
//        ) else if (System.currentTimeMillis() - mExitPressTime < 2000) super.onBackPressed() else {
//            mExitPressTime = System.currentTimeMillis()
//            Toast.makeText(this, R.string.mesg_secureExit, Toast.LENGTH_SHORT).show()
//        }
//    }

//    override fun onUserProfileUpdated() {
//        createHeaderView()
//    }

    private fun applyAwaitingDrawerAction() {
        if (pendingMenuItemId == 0) {
            return // drawer was opened, but nothing was clicked.
        }

        when (pendingMenuItemId) {
            R.id.menu_activity_main_manage_devices -> {
                startActivity(Intent(this, ManageDevicesActivity::class.java))
            }
            R.id.menu_activity_main_about -> startActivity(Intent(this, AboutActivity::class.java))
            R.id.menu_activity_main_send_application -> ShareAppDialog(this).show()
            R.id.menu_activity_main_preferences -> {
                startActivity(Intent(this, PreferencesActivity::class.java))
            }
            R.id.menu_activity_main_donate -> try {
                startActivity(
                    Intent(
                        applicationContext,
                        Class.forName("org.monora.uprotocol.client.android.activity.DonationActivity")
                    )
                )
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
//            R.id.menu_activity_feedback -> Activities.startFeedbackActivity(this)
        }

//        else if (R.id.menu_activity_main_manage_devices == mChosenMenuItemId) {
//            startActivity(Intent(this, ManageDevicesActivity::class.java))
//        } else if (R.id.menu_activity_main_about == mChosenMenuItemId) {
//            startActivity(Intent(this, AboutActivity::class.java))
//        } else if (R.id.menu_activity_main_send_application == mChosenMenuItemId) {
//            ShareAppDialog(this@MainActivity)
//                .show()
//        } else if (R.id.menu_activity_main_preferences == mChosenMenuItemId) {
//            startActivity(Intent(this, PreferencesActivity::class.java))
//            //        } else if (R.id.menu_activity_main_exit == mChosenMenuItemId) {
////            exitApp();
////        } else if (R.id.menu_activity_main_donate == mChosenMenuItemId) {
////            try {
////                startActivity(new Intent(this, Class.forName("com.genonbeta.TrebleShot.activity.DonationActivity")));
////            } catch (ClassNotFoundException e) {
////                e.printStackTrace();
////            }
//        } else if (R.id.menu_activity_main_dev_survey == mChosenMenuItemId) {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle(R.string.text_developmentSurvey)
//            builder.setMessage(R.string.text_developmentSurveySummary)
//            builder.setNegativeButton(R.string.genfw_uwg_later, null)
//            builder.setPositiveButton(
//                R.string.butn_temp_doIt,
//                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
//                    try {
//                        startActivity(
//                            Intent(Intent.ACTION_VIEW).setData(
//                                Uri.parse(
//                                    "https://docs.google.com/forms/d/e/1FAIpQLSc9S-It3mgp9Bw_SBIHXf-Kyt6-rm02SR5-jn_ipu5DfbUKjg/viewform?usp=sf_link"
//                                )
//                            )
//                        )
//                    } catch (e: ActivityNotFoundException) {
//                        Toast.makeText(
//                            this@MainActivity, R.string.mesg_temp_noBrowser,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//            builder.show()
//        } else if (R.id.menu_activity_feedback == mChosenMenuItemId) {
//            AppUtils.createFeedbackIntent(this@MainActivity)
//        } else if (R.id.rate_us == mChosenMenuItemId) {
//            val uri = Uri.parse(AppConfig.URI_MARKET)
//            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
//            // To count with Play market backstack, After pressing back button,
//            // to taken back to our application, we need to add following flags to intent.
//            goToMarket.addFlags(
//                Intent.FLAG_ACTIVITY_NO_HISTORY or
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
//            )
//            try {
//                startActivity(goToMarket)
//            } catch (e: ActivityNotFoundException) {
//                startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse(AppConfig.URI_GOOGLE_PLAY)
//                    )
//                )
//            }
//        }
        pendingMenuItemId = 0
    }

//    private fun createHeaderView() {
//        val headerView: View = mNavigationView.getHeaderView(0)
//        val surveyItem: MenuItem =
//            mNavigationView.getMenu().findItem(R.id.menu_activity_main_dev_survey)
//        val configuration = application.resources.configuration
//        if (Build.VERSION.SDK_INT >= 24) {
//            val list: LocaleList = configuration.locales
//            if (list.size() > 0) for (pos in 0 until list.size()) if (list.get(pos).toLanguageTag()
//                    .startsWith("en")
//            ) {
//                surveyItem.isVisible = true
//                break
//            }
//        } else surveyItem.isVisible = configuration.locale.toString().startsWith("en")
//        if (headerView != null) {
//            val localDevice: NetworkDevice = AppUtils.getLocalDevice(applicationContext)
//            val imageView =
//                headerView.findViewById<ImageView>(R.id.layout_profile_picture_image_default)
//            val editImageView =
//                headerView.findViewById<ImageView>(R.id.layout_profile_picture_image_preferred)
//            val deviceNameText: TextView =
//                headerView.findViewById<TextView>(R.id.header_default_device_name_text)
//            val versionText: TextView =
//                headerView.findViewById<TextView>(R.id.header_default_device_version_text)
//            deviceNameText.setText(localDevice.nickname)
//            versionText.setText(localDevice.versionName)
//            loadProfilePictureInto(localDevice.nickname, imageView)
//            editImageView.setOnClickListener { v: View? -> startProfileEditor() }
//        }
//    }

//    val powerfulActionMode: PowerfulActionMode
//        get() = mActionMode

    private fun highlightUpdater() {
//        val item: MenuItem = mNavigationView.getMenu().findItem(R.id.menu_activity_main_about)
//        item.setTitle(R.string.text_newVersionAvailable)
        navigationView.menu.findItem(R.id.menu_activity_main_about).setTitle(R.string.text_newVersionAvailable)
    }

//    fun requestTrustZoneStatus() {
//        AppUtils.startForegroundService(
//            this, Intent(this, CommunicationService::class.java)
//                .setAction(CommunicationService.ACTION_REQUEST_TRUSTZONE_STATUS)
//        )
//    }

//    fun toggleTrustZone() {
//        AppUtils.startForegroundService(
//            this, Intent(this, CommunicationService::class.java)
//                .setAction(CommunicationService.ACTION_TOGGLE_SEAMLESS_MODE)
//        )
//    }

//    private inner class ActivityReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            if (CommunicationService.ACTION_TRUSTZONE_STATUS == intent.getAction() && mTrustZoneToggle != null) mTrustZoneToggle.setTitle(
//                if (intent.getBooleanExtra(
//                        CommunicationService.EXTRA_STATUS_STARTED, false
//                    )
//                ) R.string.butn_turnTrustZoneOff else R.string.butn_turnTrustZoneOn
//            )
//        }
//    }

    companion object {
        const val REQUEST_PERMISSION_ALL = 1
    }
}