package com.example.moviestmdb

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.moviestmdb.core.di.ApplicationScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MoviesApplication: Application() {

    @Inject
    lateinit var mainLifecycleListener: MainLifecycleListener

    private val lifecycleListener: MainLifecycleListener by lazy {
        mainLifecycleListener
    }

    override fun onCreate() {
        super.onCreate()
        setupLifecycleListener()

        Timber.plant(Timber.DebugTree())
    }



    private fun setupLifecycleListener() {
        ProcessLifecycleOwner.get()
            .lifecycle.addObserver(lifecycleListener)
    }
}