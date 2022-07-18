package com.example.moviestmdb.ui_movies.fragments.lobby_fragment

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
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.FragmentLobbyBinding
import com.example.moviestmdb.ui_movies.lobby.NowPlayingMoviesCarrouselAdapter
import com.example.moviestmdb.ui_movies.lobby.PopularMoviesCarrouselAdapter
import com.example.moviestmdb.ui_movies.lobby.TopRatedMoviesCarrouselAdapter
import com.example.moviestmdb.ui_movies.lobby.UpcomingMoviesCarrouselAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MoviesLobbyFragment : Fragment() {

    lateinit var binding: FragmentLobbyBinding
    private val viewModel: MovieLobbyViewModel by viewModels()

    lateinit var popularMoviesAdapter: PopularMoviesCarrouselAdapter
    lateinit var upcomingMoviesAdapter: UpcomingMoviesCarrouselAdapter
    lateinit var topRatedMoviesAdapter: TopRatedMoviesCarrouselAdapter
    lateinit var nowPlayingMoviesAdapter: NowPlayingMoviesCarrouselAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLobbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPopularAdapter()
        initUpcomingAdapter()
        initNowPlayingAdapter()
        initTopRatedAdapter()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.state.collect { uiState ->

                binding.swipeRefresh.isRefreshing = uiState.refreshing

                uiState.message?.let { message ->
                    Snackbar.make(requireView(), message.message, Snackbar.LENGTH_LONG)
                        .setAction("Dismiss") {
                            viewModel.clearMessage(message.id)
                        }
                        .show()
                }

                binding.popularMoviesView.setLoading(uiState.popularRefreshing)
                popularMoviesAdapter.submitList(uiState.popularMovies)

                binding.topRatedMoviesView.setLoading(uiState.topRatedRefreshing)
                topRatedMoviesAdapter.submitList(uiState.topRatedMovies)

                binding.upcomingMoviesView.setLoading(uiState.upcomingRefreshing)
                upcomingMoviesAdapter.submitList(uiState.upcomingMovies)

                binding.nowPlayingMoviesView.setLoading(uiState.nowPlayingRefreshing)
                nowPlayingMoviesAdapter.submitList(uiState.nowPlayingMovies)
            }
        }
    }

    private val onMovieClick: (Int) -> Unit = { movieId ->
        Timber.i("$movieId")
    }

    private fun initPopularAdapter() {
        popularMoviesAdapter = PopularMoviesCarrouselAdapter(
            tmdbImageManager.getLatestImageProvider(),
            onMovieClick,
        )
        binding.popularMoviesView.recyclerView.run {
            adapter = popularMoviesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            val spacing = 20
            addItemDecoration(SpaceItemDecoration(
                spacing, -spacing
            ))
        }

        binding.popularMoviesView.title.text = "Popular Movies"
        binding.popularMoviesView.more.setOnClickListener {
        findNavController().navigate(R.id.navigateToPopularMoviesFragment)
        }
    }

    private fun initNowPlayingAdapter() {
        nowPlayingMoviesAdapter = NowPlayingMoviesCarrouselAdapter(
            tmdbImageManager.getLatestImageProvider(),
            onMovieClick,
        )
        binding.nowPlayingMoviesView.recyclerView.run {
            adapter = nowPlayingMoviesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            val spacing = 20
            addItemDecoration(SpaceItemDecoration(
                spacing, -spacing
            ))
        }

        binding.nowPlayingMoviesView.title.text = "nowPlaying Movies"
        binding.nowPlayingMoviesView.more.setOnClickListener {
        findNavController().navigate(R.id.navigateToNowPlayingMoviesFragment)
        }
    }

    private fun initUpcomingAdapter() {
        upcomingMoviesAdapter = UpcomingMoviesCarrouselAdapter(
            tmdbImageManager.getLatestImageProvider(),
            onMovieClick,
        )
        binding.upcomingMoviesView.recyclerView.run {
            adapter = upcomingMoviesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            val spacing = 20
            addItemDecoration(SpaceItemDecoration(
                spacing, -spacing
            ))
        }

        binding.upcomingMoviesView.title.text = "upcoming Movies"
        binding.upcomingMoviesView.more.setOnClickListener {
        findNavController().navigate(R.id.navigateToUpcomingMoviesFragment)
        }
    }

    private fun initTopRatedAdapter() {
        topRatedMoviesAdapter = TopRatedMoviesCarrouselAdapter(
            tmdbImageManager.getLatestImageProvider(),
            onMovieClick,
        )
        binding.topRatedMoviesView.recyclerView.run {
            adapter = topRatedMoviesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            val spacing = 20
            addItemDecoration(SpaceItemDecoration(
                spacing, -spacing
            ))
        }

        binding.topRatedMoviesView.title.text = "topRated Movies"
        binding.topRatedMoviesView.more.setOnClickListener {
        findNavController().navigate(R.id.navigateToTopRatedMoviesFragment)
        }
    }
}