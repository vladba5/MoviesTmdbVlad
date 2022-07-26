package com.example.moviestmdb.ui_login

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moviestmdb.ui_login.databinding.LoginFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: LoginFragmentBinding
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

        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val pass = binding.passwordInput.text.toString()
            if(email.isNotBlank() && pass.isNotBlank()){
                viewModel.loginUser(email, pass)
            }else{
                showDialog("please enter valid text")
            }
        }


//        launchAndRepeatWithViewLifecycle {
//            viewModel.authenticationState.collect {
//                when (it) {
//                    LoginViewModel.AuthenticationClass.AUTHENTICATED -> {
//                        val request = NavDeepLinkRequest.Builder
//                            .fromUri("android-app://example.tmdb.app/lobby_fragment".toUri())
//                            .build()
//                        findNavController().navigate(request)
//                    }
//                    LoginViewModel.AuthenticationClass.UNAUTHENTICATED ->
//                        Timber.i("vlad fb UNAUTHENTICATED")
//                }
//            }
//        }
    }


//    private fun updateUI(user: FirebaseUser?) {
//        context?.showToast("Sign in success ${user.toString()}")
//
//        user?.let {
//            val request = NavDeepLinkRequest.Builder
//                .fromUri("android-app://example.tmdb.app/lobby_fragment".toUri())
//                .build()
//            findNavController().navigate(request)
//        }
//    }


    fun showDialog(txt: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("User login")
        builder.setMessage(txt)
        builder.setNegativeButton(txt){_,_->

        }
        builder.setPositiveButton("Ok") { dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}