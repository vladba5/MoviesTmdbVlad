package com.example.moviestmdb.core_ui.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, length).show()

    fun View.showSnackBar(message: String, length: Int = Snackbar.LENGTH_SHORT) =
        Snackbar.make(this, message, length).show()

    fun <T>View.showSnackBarWithAction(
        message: String, length: Int = Snackbar.LENGTH_SHORT,
        actionMessage: String = "Dismiss", function: (element: T) -> Unit
    ) =
        Snackbar.make(this, message, length)
            .setAction(actionMessage) { function }.show()


//    fun Context.checkIfConnected() : Flow<Boolean> = callbackFlow {
//        val callBack = object : FirebaseAuth.AuthStateListener
//    }
