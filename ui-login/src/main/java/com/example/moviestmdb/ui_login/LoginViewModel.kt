package com.example.moviestmdb.ui_login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map

class LoginViewModel : ViewModel() {

    enum class AuthenticationClass {
        AUTHENTICATED,
        UNAUTHENTICATED
    }

    val authenticationState = FireBaseManager().getUserInfo().map {
        if (it != null) {
            AuthenticationClass.AUTHENTICATED
        } else {
            AuthenticationClass.UNAUTHENTICATED
        }
    }
}