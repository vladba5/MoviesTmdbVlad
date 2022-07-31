package com.example.ui_profile.profile_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.constants.Constants
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_movies.popular.FavoriteMoviesAdapter
import com.example.ui_profile.databinding.ProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var binding: ProfileFragmentBinding
    private val viewModel: ProfileViewModel by viewModels()
    lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFavoriteAdapter()

        binding.profileLogOutBtn.setOnClickListener {
            viewModel.logOut()
        }

        binding.profileUpdateBtn.setOnClickListener {
            viewModel.updateData(123)
        }

        binding.profileFavoriteBtn.setOnClickListener {
            viewModel.removeData(123)
        }
    }

    private fun initFavoriteAdapter() {
        favoriteMoviesAdapter = FavoriteMoviesAdapter(
            tmdbImageManager.getLatestImageProvider(),
            onMovieClick,
        )
        binding.favoriteRecycler.run {
            adapter = favoriteMoviesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            val spacing = 20
            addItemDecoration(
                SpaceItemDecoration(
                    spacing, -spacing
                )
            )
        }
    }


    private val onMovieClick: (Int) -> Unit = { movieId ->
        context?.showToast(movieId.toString())

//        val args = Bundle();
//        args.putInt(Constants.MOVIE_ID, movieId)
//        findNavController().navigate(R.id.movieDetailsFragment, args)
    }

}