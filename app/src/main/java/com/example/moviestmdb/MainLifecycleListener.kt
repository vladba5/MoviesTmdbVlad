package com.example.moviestmdb

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.moviestmdb.core.di.ApplicationScope
import com.example.moviestmdb.core.managers.FireBaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainLifecycleListener @Inject constructor(
    @ApplicationScope val applicationScope: CoroutineScope,
    private val fireBaseManager: FireBaseManager
) : DefaultLifecycleObserver {
    private var lastTime : LocalDateTime = LocalDateTime.now()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.i("vlad onStart Listener")

        val duration = Duration.between(lastTime, LocalDateTime.now()).seconds
        if(duration >= Duration.ofMinutes(1).seconds){
            invokeLogOut()
        }
    }

    private fun invokeLogOut() {
        Timber.i("vlad logout")
        applicationScope.launch {
            fireBaseManager.logout()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.i("vlad onStop Listener")
        lastTime = LocalDateTime.now()
    }
}