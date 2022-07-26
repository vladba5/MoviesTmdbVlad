package com.example.ui_profile.profile_fragment

import androidx.lifecycle.ViewModel
import com.example.moviestmdb.core.managers.FireBaseManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val fireBaseManager: FireBaseManager,
): ViewModel() {

    fun logOut(){
        fireBaseManager.logout()
    }
}