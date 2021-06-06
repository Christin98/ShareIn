package com.project.christinkcdev.share.sharein.util

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.project.christinkcdev.share.sharein.BuildConfig
import com.project.christinkcdev.share.sharein.config.AppConfig
import com.project.christinkcdev.share.sharein.data.GitHubDataRepository
import com.project.christinkcdev.share.sharein.remote.model.Release
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Updater @Inject constructor(
    @ApplicationContext context: Context,
    private val gitHubDataRepository: GitHubDataRepository,
) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    suspend fun checkForUpdates(): Release? {
        return GitHubUpdater.checkForUpdates(gitHubDataRepository)?.also { release ->
            preferences.edit {
                putString("availableVersion", release.tag)
                putLong("checkedForUpdatesTime", System.currentTimeMillis())
            }
        }
    }

    fun declareLatestChangelogAsShown() = preferences.edit {
        putInt("changelog_seen_version", BuildConfig.VERSION_CODE)
    }

    private fun getAvailableVersion(): String? {
        return preferences.getString("availableVersion", null)
    }

    fun hasNewVersion(): Boolean {
        val availableVersion = getAvailableVersion()
        return availableVersion != null && GitHubUpdater.isNewerVersion(availableVersion)
    }

    fun isLatestChangelogShown(): Boolean {
        val lastSeenChangelog = preferences.getInt("changelog_seen_version", -1)
        val dialogAllowed = preferences.getBoolean("show_changelog_dialog", true)
        return !preferences.contains("previously_migrated_version")
                || BuildConfig.VERSION_CODE == lastSeenChangelog
                || !dialogAllowed
    }

    fun needsToCheckForUpdates(): Boolean {
        val checkedOn = preferences.getLong("checkedForUpdatesTime", 0)

        return BuildConfig.FLAVOR != "googlePlay" && !hasNewVersion()
                && System.currentTimeMillis() - checkedOn >= AppConfig.DELAY_UPDATE_CHECK
    }

}


