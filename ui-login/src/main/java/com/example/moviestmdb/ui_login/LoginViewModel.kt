package com.example.moviestmdb.ui_login

import androidx.lifecycle.ViewModel
import com.example.moviestmdb.core.managers.FireBaseManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val fireBaseManager : FireBaseManager
) : ViewModel() {

    fun loginUser(email: String, pass: String){
        fireBaseManager.loginUser(email, pass)
    }

//    enum class AuthenticationClass {
//        AUTHENTICATED,
//        UNAUTHENTICATED
//    }
//
//    val authenticationState = FireBaseManager().getUserInfo().map {
//        if (it != null) {
//            AuthenticationClass.AUTHENTICATED
//        } else {
//            AuthenticationClass.UNAUTHENTICATED
//        }
//    }

}