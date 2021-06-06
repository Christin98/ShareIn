package com.project.christinkcdev.share.sharein.data

import com.project.christinkcdev.share.sharein.remote.GitHubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubDataRepository @Inject constructor(
    private val gitHubService: GitHubService,
){
    suspend fun getContributors() = withContext(Dispatchers.IO) { gitHubService.contributors() }

    suspend fun getReleases() = withContext(Dispatchers.IO) { gitHubService.releases() }
}