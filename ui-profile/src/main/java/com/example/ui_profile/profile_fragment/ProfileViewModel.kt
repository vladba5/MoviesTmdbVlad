package com.example.ui_profile.profile_fragment

import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.managers.FireBaseManager
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.ui_movies.popular.FavoriteMoviesAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val fireBaseManager: FireBaseManager,
): ViewModel() {

    fun logOut(){
        fireBaseManager.logout()
    }

    fun updateData(movieId: Int){
        fireBaseManager.insertFavoriteMovies(movieId)
    }


    fun removeData(movieId: Int){
        fireBaseManager.removeFavoriteMovies(movieId)
    }

}