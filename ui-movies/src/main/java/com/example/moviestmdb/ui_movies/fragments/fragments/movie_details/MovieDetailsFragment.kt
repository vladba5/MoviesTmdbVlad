package com.example.moviestmdb.ui_movies.fragments.fragments.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core.util.UiMessage
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showSnackBarWithAction
import com.example.moviestmdb.ui_movies.databinding.FragmentMovieDetailsBinding
import com.example.moviestmdb.ui_movies.lobby.ActorsMoviesAdapter
import com.example.moviestmdb.ui_movies.lobby.RecommendedMoviesAdapter
import com.example.moviestmdb.util.TmdbImageUrlProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel: MovieDetailsViewModel by viewModels()

    lateinit var actorsMoviesAdapter: ActorsMoviesAdapter
    lateinit var recommendedMoviesAdapter: RecommendedMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    @Inject
    lateinit var tmdbImageUrlProvider: TmdbImageUrlProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun clearMessage(message: UiMessage) {
        viewModel.clearMessage(message.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(binding.toolbar, findNavController())

        initRecommendedAdapter()
        initActorsAdapter()

        launchAndRepeatWithViewLifecycle {
            viewModel.detailState.collect { uiState ->

               // binding.swipeRefresh.isRefreshing = uiState.refreshing

                uiState.message?.let { message ->
                    view.showSnackBarWithAction(
                        message = message.message,
                        actionMessage = "Dismiss",
                        function = this@MovieDetailsFragment::clearMessage
                    )
                }

                loadBindingData(binding, uiState)
            }
        }

//        binding.swipeRefresh.setOnRefreshListener {
//            viewModel.refresh()
//        }
    }

    private fun loadBindingData(
        binding: FragmentMovieDetailsBinding,
        detailsUiState: DetailsUiState
    ) {
        binding.toolbar.title = "Detail Screen"

            binding.favoriteSwitch.setOnCheckedChangeListener{ _, isChecked ->
                if(isChecked){
                    viewModel.addMovie(123) //detailsUiState.movie.id
                }else{
                    viewModel.removeMovie(123)
            }
        }

        detailsUiState.movie?.let { movie ->

            movie.posterPath?.let { poster ->
                Glide
                    .with(binding.root)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = poster,
                            imageWidth = binding.movieImg.width
                        )
                    )
                    .into(binding.movieImg)
            }

            movie.backdropPath?.let { movieImg ->
                Glide
                    .with(binding.root)
                    .load(
                        tmdbImageUrlProvider.getBackdropUrl(
                            path = movieImg,
                            imageWidth = binding.wallpaperImg.width
                        )
                    )
                    .into(binding.wallpaperImg)
            }

            binding.ratePercentTxt.text = (movie.voteAverage?.times(10)).toString()
            binding.voteAmountTxt.text = movie.voteCount?.toString()
            //val released = Date(cu).after(release_date)
            binding.statusTxt.text = "released"
            binding.budgetTxt.text = "34234209348"
            binding.revenueTxt.text = "23048224"
            binding.descriptionTxt.text = movie.overView

            binding.favoriteSwitch.isEnabled = detailsUiState.isFavorite
        }

        recommendedMoviesAdapter.submitList(detailsUiState.recommendedMovies)
        actorsMoviesAdapter.submitList(detailsUiState.actorList)
    }

    private fun initRecommendedAdapter() {
        recommendedMoviesAdapter = RecommendedMoviesAdapter(
            tmdbImageManager.getLatestImageProvider()
        )
        binding.recommendedRecycler.run {
            adapter = recommendedMoviesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            val spacing = 20
            addItemDecoration(
                SpaceItemDecoration(
                    spacing, -spacing
                )
            )
        }
    }

    private fun initActorsAdapter() {
        actorsMoviesAdapter = ActorsMoviesAdapter(
            tmdbImageManager.getLatestImageProvider()
        )
        binding.actorsRecycler.run {
            adapter = actorsMoviesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            val spacing = 20
            addItemDecoration(
                SpaceItemDecoration(
                    spacing, -spacing
                )
            )
        }
    }
}