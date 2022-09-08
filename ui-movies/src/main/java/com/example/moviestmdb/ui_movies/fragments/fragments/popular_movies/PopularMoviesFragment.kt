package com.example.moviestmdb.ui_movies.popular

import android.content.Context
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
import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.constants.Constants
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core.util.UiMessage
import com.example.moviestmdb.core_ui.R.dimen
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.ui_movies.databinding.FragmentPopularMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.example.moviestmdb.core_ui.util.*
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.example.moviestmdb.ui_movies.fragments.fragments.popular_movies.PopularViewState
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PopularMoviesFragment: Fragment() {

    private val viewModel: PopularMoviesViewModel by viewModels()
    lateinit var binding: FragmentPopularMoviesBinding
    lateinit var pagingAdapter: PopularMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularMoviesBinding.inflate(inflater)

        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "Popular Movies"

        launchAndRepeatWithViewLifecycle {
            viewModel.pageList.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.filteredChips.collect { chips ->
                val list = mutableListOf<Chip>()
                chips.forEach { chip ->
                    val ch = createChip(chip)
                    ch.setOnCheckedChangeListener { compoundButton, isChecked ->
                        viewModel.toggleFilter(compoundButton.id, isChecked)
                    }
                    list.add(ch)
                }

                binding.popularChipGroup.removeAllViews()
                list.forEach { chip ->
                    binding.popularChipGroup.addView(chip)
                }
            }
        }
    }

    private fun createChip(chip: Genre): Chip {
        val chipView = ChipBinding.inflate(LayoutInflater.from(context)).root
        chipView.id = chip.id
        chipView.text = chip.name

        return chipView
    }

    private val movieClickListener : (Int) -> Unit = { movieId ->
        context?.showToast(movieId.toString())

        val args = Bundle();
        args.putInt(Constants.MOVIE_ID, movieId)
        findNavController().navigate(R.id.movieDetailsFragment, args)
    }

    private fun initAdapter() {
        pagingAdapter =
            PopularMoviesAdapter(
                tmdbImageManager.getLatestImageProvider(),
                movieClickListener
            )

        binding.popularRecycler.run {
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