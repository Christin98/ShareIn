package com.project.christinkcdev.share.sharein

import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import androidx.core.content.edit
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import com.project.christinkcdev.share.sharein.util.Updater
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.util.*

@HiltAndroidApp
class App : MultiDexApplication(), Thread.UncaughtExceptionHandler {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private lateinit var crashFile: File

    private var defaultExceptionHandler: Thread.UncaughtExceptionHandler? = null

    override fun onCreate() {
        super.onCreate()
        crashFile = applicationContext.getFileStreamPath(FILENAME_UNHANDLED_CRASH_LOG)
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        initializeSettings()

        val updater = EntryPoints.get(this@App, AppEntryPoint::class.java).updater()

        if (updater.needsToCheckForUpdates()) applicationScope.launch {
            try {
                updater.checkForUpdates()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun initializeSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val hasNsdSet = preferences.contains("nsd_enabled")
        val hasReferralVersion = preferences.contains("referral_version")

        PreferenceManager.setDefaultValues(this, R.xml.preferences_defaults_main, false)

        if (!hasReferralVersion) preferences.edit {
            putInt("referral_version", BuildConfig.VERSION_CODE)
        }

        // Some pre-kitkat devices were soft rebooting when this feature was turned on by default.
        // So we will disable it for them and it will still remain as an option for the user.
        if (!hasNsdSet) preferences.edit {
            putBoolean("nsd_enabled", Build.VERSION.SDK_INT >= 19)
        }

        val migratedVersion = preferences.getInt("migrated_version", MIGRATION_NONE)
        if (migratedVersion < BuildConfig.VERSION_CODE) preferences.edit {
            putInt("migrated_version", BuildConfig.VERSION_CODE)
            if (migratedVersion > MIGRATION_NONE) putInt("previously_migrated_version", migratedVersion)
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            if (crashFile.canWrite()) {
                Log.d(TAG, "uncaughtException: Check failed")
                return
            }

            PrintWriter(FileOutputStream(crashFile)).use { printWriter ->
                val stackTrace = e.stackTrace

                printWriter.append("-- ShareIn Crash Log ---\n")
                    .append("\nException: ${e.javaClass.simpleName}")
                    .append("\nMessage: ${e.message}")
                    .append("\nCause: ${e.cause}")
                    .append("\nDate: ")
                    .append(DateFormat.getLongDateFormat(this).format(Date()))
                    .append("\n\n--Stacktrace--\n\n")

                if (stackTrace.isNotEmpty()) for (element in stackTrace) with(element) {
                    printWriter.append("$className.$methodName:$lineNumber\n")
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            defaultExceptionHandler?.uncaughtException(t, e)
        }
    }

    companion object {
        val TAG = App::class.simpleName

        private const val MIGRATION_NONE = -1

        const val FILENAME_UNHANDLED_CRASH_LOG = "sharein_unhandled_crash_log.txt"
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun updater(): Updater
}