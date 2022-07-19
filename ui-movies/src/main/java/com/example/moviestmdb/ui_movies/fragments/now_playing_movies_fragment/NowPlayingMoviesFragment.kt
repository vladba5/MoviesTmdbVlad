package com.example.moviestmdb.ui_movies.fragments.now_playing_movies_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.R.dimen
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.FragmentNowPlayingMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.example.moviestmdb.core.constants.Constants
import com.example.moviestmdb.core.constants.Constants.MOVIE_ID

@AndroidEntryPoint
class NowPlayingMoviesFragment : Fragment() {

    lateinit var binding: FragmentNowPlayingMoviesBinding
    private val viewModel: NowPlayingMoviesViewModel by viewModels()

    lateinit var pagingAdapter: NowPlayingMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowPlayingMoviesBinding.inflate(inflater)

        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "Now Playing Movies"

        launchAndRepeatWithViewLifecycle {
            viewModel.pagedList.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private val movieClickListener: (Int) -> Unit = { movieId ->
        context?.showToast(movieId.toString())

        val args = Bundle();
        args.putInt(MOVIE_ID, movieId)
        findNavController().navigate(R.id.movieDetailsFragment, args)
    }

    private fun initAdapter() {
        pagingAdapter =
            NowPlayingMoviesAdapter(
                tmdbImageManager.getLatestImageProvider(),
                movieClickListener
            )

        binding.nowPlayingRecycler.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = pagingAdapter

            val spacing = resources.getDimension(dimen.spacing_normal).toInt()
            val decoration = SpaceItemDecoration(
                spacing, -spacing
            )
            addItemDecoration(decoration)
        }
    }

}