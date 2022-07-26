package com.example.moviestmdb

import androidx.lifecycle.ViewModel
import com.example.moviestmdb.core.managers.FireBaseManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val fireBaseManager: FireBaseManager
) : ViewModel() {

    fun loginState(): Flow<Boolean> {
        return fireBaseManager.isConnected()
    }
}