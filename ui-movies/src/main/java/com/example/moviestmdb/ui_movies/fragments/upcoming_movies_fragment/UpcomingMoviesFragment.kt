package com.example.moviestmdb.ui_movies.fragments.upcoming_movies_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.constants.Constants
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.R.dimen
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.FragmentUpcomingMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class UpcomingMoviesFragment: Fragment() {

    lateinit var binding: FragmentUpcomingMoviesBinding
    private val viewModel: UpcomingMoviesViewModel by viewModels()

    lateinit var pagingAdapter: UpcomingMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingMoviesBinding.inflate(inflater)

        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "Upcoming Movies"

        launchAndRepeatWithViewLifecycle {
            viewModel.pagedList.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private val movieClickListener : (Int) -> Unit = { movieId ->
        context?.showToast(movieId.toString())

        val args = Bundle();
        args.putInt(Constants.MOVIE_ID, movieId)
        findNavController().navigate(R.id.movieDetailsFragment, args)
    }

    private fun initAdapter() {
        pagingAdapter =
            UpcomingMoviesAdapter(
                tmdbImageManager.getLatestImageProvider(),
                movieClickListener
            )

        binding.upcomingRecycler.run {
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