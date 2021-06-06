package com.project.christinkcdev.share.sharein.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.project.christinkcdev.share.sharein.data.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject internal constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    val client = userDataRepository.client()

    val clientStatic
        get() = userDataRepository.clientStatic()

    val deletePictureListener = View.OnClickListener {
        Graphics.deleteLocalClientPicture(it.context)
    }
}