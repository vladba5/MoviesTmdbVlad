package com.example.moviestmdb.ui_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviestmdb.core_ui.util.showSnackBar
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_login.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class LoginFragment : Fragment() {


    lateinit var binding: LoginFragmentBinding
    private lateinit var mAuth: FirebaseAuth;

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
            //context?.showToast("login")
            //it.showSnackBar("login")
//            val currentUser = mAuth.currentUser
//            if(currentUser != null){
//                login()
//            }
            login(view)
        }
    }

    fun login(view: View){
        val pass = binding.passwordInput.text.toString()
        val email = binding.emailInput.text.toString()

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("vlad Sign in success")
                    val user = mAuth.currentUser
                    context?.showToast("Sign in success ${user.toString()}")
                    //updateUI(user)
                } else {
                    Timber.d("vlad Sign in fail ${task.exception}")
                    context?.showToast("Sign in fail ${task.exception}")
                    //updateUI(null)
                }
            }
    }

}