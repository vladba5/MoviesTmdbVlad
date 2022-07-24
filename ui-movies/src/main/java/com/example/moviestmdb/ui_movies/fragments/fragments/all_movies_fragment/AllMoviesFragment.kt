package com.example.moviestmdb.ui_movies.fragments.fragments.all_movies_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.ui_movies.databinding.FragmentAllMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AllMoviesFragment : Fragment() {

    lateinit var binding: FragmentAllMoviesBinding
    //private val viewModel: AllMoviesViewModel by viewModels()

    lateinit var movieRowAdapter: AllMoviesMovieRowAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllMoviesAdapter()

//        movieRowAdapter.submitList(viewModel.getInitialList())
    }

    private val onMovieClick: (Int) -> Unit = { movieId ->
        Timber.d("movie id is $movieId")
    }

    private fun initAllMoviesAdapter() {
        movieRowAdapter = AllMoviesMovieRowAdapter(
            tmdbImageManager.getLatestImageProvider(),
            onMovieClick,
        )

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.allMoviesRecycler.run {
            adapter = movieRowAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(decoration)

            val spacing = 30
            addItemDecoration(
                SpaceItemDecoration(
                    spacing, -spacing
                )
            )
        }
    }

    private fun initPagination(storeType : Int) {
        launchAndRepeatWithViewLifecycle {
//            viewModel.getListData(storeType).collectLatest {
//               // movieRowAdapter.submitData(it)
//            }
        }

    }


}