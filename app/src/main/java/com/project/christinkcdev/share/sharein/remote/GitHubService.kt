package com.project.christinkcdev.share.sharein.remote

import com.project.christinkcdev.share.sharein.config.AppConfig.URI_REPO
import com.project.christinkcdev.share.sharein.remote.model.Contributor
import com.project.christinkcdev.share.sharein.remote.model.Release
import retrofit2.http.GET

interface GitHubService {
    @GET("$URI_REPO/contributors")
    suspend fun contributors(): List<Contributor>

    @GET("$URI_REPO/releases")
    suspend fun releases(): List<Release>
}