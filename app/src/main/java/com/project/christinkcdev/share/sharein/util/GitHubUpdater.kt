package com.project.christinkcdev.share.sharein.util

import android.util.Log
import com.project.christinkcdev.share.sharein.BuildConfig
import com.project.christinkcdev.share.sharein.data.GitHubDataRepository
import com.project.christinkcdev.share.sharein.remote.model.Release
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.maven.artifact.versioning.ComparableVersion

object GitHubUpdater {
    private val TAG = GitHubUpdater::class.simpleName

    suspend fun checkForUpdates(gitHubDataRepository: GitHubDataRepository): Release? {
        Log.d(TAG, "Checking for updates")

        val releases = withContext(Dispatchers.IO) { gitHubDataRepository.getReleases() }
        val installed = ComparableVersion(BuildConfig.VERSION_NAME)

        Log.d(TAG, "Gathered the results with length=" + releases.size)

        releases.forEach { release ->
            val installable = ComparableVersion(release.tag)
            val upgradable = installable > installed

            if (release.prerelase || release.assets.isNullOrEmpty() || !upgradable) {
                return@forEach
            }

            return release
        }

        return null
    }

    fun isNewerVersion(versionName: String): Boolean {
        val installable = ComparableVersion(versionName)
        val installed = ComparableVersion(BuildConfig.VERSION_NAME)

        return installable > installed
    }
}