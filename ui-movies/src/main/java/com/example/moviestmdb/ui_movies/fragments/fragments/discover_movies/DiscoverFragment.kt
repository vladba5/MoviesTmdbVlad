package com.example.moviestmdb.ui_movies.fragments.fragments.discover_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.FragmentDiscoverMoviesBinding
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterBottomSheet
import com.example.moviestmdb.ui_movies.fragments.view_holder.MovieAndGenre
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DiscoverFragment : Fragment() {
    lateinit var binding: FragmentDiscoverMoviesBinding
    private val viewModel: DiscoverViewModel by viewModels()
    lateinit var discoverAdapter: DiscoverMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    var bottomTag: String = "FilterBottomSheet"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDiscoverAdapter()
        initVars()

        launchAndRepeatWithViewLifecycle {
            viewModel.getPagingData().collectLatest{ pagingData ->
                val data = pagingData.map {
                    MovieAndGenre(it, viewModel.genres.first())
                }
                discoverAdapter.submitData(data)
            }
        }
    }

    private fun initVars() {
        setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "Discover Movies"
        binding.toolbar.inflateMenu(R.menu.filter_menu)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter_movies -> showFilterBottomSheet()
            }
            true
        }
    }

    private fun showFilterBottomSheet() {
        val callback: (response: HashMap<String, String>) -> Unit = { filterData ->
            Timber.i("$filterData")
        }

        val filterBottomSheet = FilterBottomSheet()
        filterBottomSheet.listener = callback
        filterBottomSheet.show(childFragmentManager, bottomTag)
    }

    fun initDiscoverAdapter() {
        discoverAdapter =
            DiscoverMoviesAdapter(
                tmdbImageManager.getLatestImageProvider(),
                movieClickListener
            )

        binding.discoverRecycler.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = discoverAdapter

            val spacing = resources.getDimension(com.example.moviestmdb.core_ui.R.dimen.spacing_normal).toInt()
            val decoration = SpaceItemDecoration(
                spacing, -spacing
            )
            addItemDecoration(decoration)
        }
    }

    private val movieClickListener: (Int) -> Unit = { movieId ->
        context?.showToast(movieId.toString())
    }
}