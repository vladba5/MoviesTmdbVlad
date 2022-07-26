package com.example.moviestmdb.core

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton


enum class LoginState {
    LOGED_IN, LOGED_OUT
}

@Singleton
class LoginHandler @Inject constructor(
    val fireBaseAuth: FirebaseAuth
) {
    private val _connectedStateFlow = MutableSharedFlow<LoginState>(replay = 1)

    fun observeState(): SharedFlow<LoginState> = _connectedStateFlow.asSharedFlow()

    fun updateStatus(loginState: LoginState) {
        _connectedStateFlow.tryEmit(loginState)
    }

    fun isConnected(): Boolean {
        return fireBaseAuth.currentUser == null
    }

    fun signOut() {
        fireBaseAuth.signOut()
    }
}
