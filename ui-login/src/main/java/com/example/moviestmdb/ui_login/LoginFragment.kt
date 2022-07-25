package com.example.moviestmdb.ui_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.util.showSnackBar
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_login.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class LoginFragment : Fragment() {


    lateinit var binding: LoginFragmentBinding
    private lateinit var mAuth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = Firebase.auth

        binding.loginBtn.setOnClickListener {
//            val currentUser = mAuth.currentUser
//            if(currentUser != null){
                login()
//            }
        }


        launchAndRepeatWithViewLifecycle{
            viewModel.authenticationState.collect{
                when(it){
                    LoginViewModel.AuthenticationClass.AUTHENTICATED -> {
                        val request = NavDeepLinkRequest.Builder
                            .fromUri("android-app://example.tmdb.app/lobby_fragment".toUri())
                            .build()
                        findNavController().navigate(request)
                    }
                    LoginViewModel.AuthenticationClass.UNAUTHENTICATED ->
                        Timber.i("vlad fb UNAUTHENTICATED")
                }
            }
        }
    }

    fun login(){
        val pass = binding.passwordInput.text.toString()
        val email = binding.emailInput.text.toString()

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("vlad Sign in success")
                    val user = mAuth.currentUser
                    updateUI(user)


                } else {
                    Timber.d("vlad Sign in fail ${task.exception}")
                    context?.showToast("Sign in fail ${task.exception}")
                    //updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        context?.showToast("Sign in success ${user.toString()}")

        user?.let {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://example.tmdb.app/lobby_fragment".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

}