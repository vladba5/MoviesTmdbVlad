package com.example.ui_profile.favorite_fragment

import androidx.lifecycle.ViewModel
import com.example.moviestmdb.core.managers.FireBaseManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    val fireBaseManager: FireBaseManager,
): ViewModel() {

}