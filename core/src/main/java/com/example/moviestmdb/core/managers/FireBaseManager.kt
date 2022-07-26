package com.example.moviestmdb.core.managers

import com.example.moviestmdb.core.data.login.FirebaseAuthStateUserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseManager @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firebaseDataBase: FirebaseDatabase,
    val firebaseAuthStateUserDataSource: FirebaseAuthStateUserDataSource
) {
    private var auth: FirebaseUser? = null

    fun loginUser(email: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("vlad Sign in success")
                    val user = firebaseAuth.currentUser
                } else {
                    Timber.d("Sign in fail ${task.exception}")
                }
            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun isConnected() : Flow<Boolean>{
        return firebaseAuthStateUserDataSource.getBasicUserInfo().map {
            it?.let {
                it.isSignedIn()
            }!!
        }
    }

    fun getDataBase() {
//        firebaseDataBase.
    }

    fun getUserInfo(): Flow<FirebaseUser?> =
        callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener {
                auth = it.currentUser
                trySend(auth)

            }
            firebaseAuth.addAuthStateListener(authStateListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
}
