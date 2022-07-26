package com.example.moviestmdb

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.moviestmdb.core.data.login.FirebaseAuthStateUserDataSource
import com.example.moviestmdb.databinding.ActivityMainBinding
import com.example.moviestmdb.ui_login.LoginViewModel
import com.example.moviestmdb.ui_login.R.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val viewmodel: MainViewModel by viewModels()

//    @Inject
//    lateinit var firebaseAuthStateUserDataSource: FirebaseAuthStateUserDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostContainer.id) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewmodel.loginState().collectLatest { connected ->
                when (connected) {
                    true -> navController.popBackStack(id.login_graph, false)
                    false -> navController.navigate(id.login_graph)
                }
            }
        }

    }
}