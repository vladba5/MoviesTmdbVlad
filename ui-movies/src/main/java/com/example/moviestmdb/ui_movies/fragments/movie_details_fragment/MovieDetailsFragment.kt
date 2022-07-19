package com.example.moviestmdb.ui_movies.fragments.movie_details_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.constants.Constants
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core.util.UiMessage
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showSnackBarWithAction
import com.example.moviestmdb.ui_movies.databinding.FragmentMovieDetailsBinding
import com.example.moviestmdb.ui_movies.lobby.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel : MovieDetailsViewModel by viewModels()

    lateinit var actorsMoviesAdapter: ActorsMoviesAdapter
    lateinit var recommendedMoviesAdapter: RecommendedMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    private var currentMovieId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if(it.containsKey(Constants.MOVIE_ID)){
                currentMovieId = it.getInt(Constants.MOVIE_ID)
                viewModel.updateData(currentMovieId)

            }
        }
    }

    fun clearMessage(message : UiMessage){
        viewModel.clearMessage(message.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(binding.toolbar, findNavController())







        loadValues(binding)



        launchAndRepeatWithViewLifecycle {
            viewModel.detailState.collect { uiState ->

                uiState.message?.let { message ->
                    view.showSnackBarWithAction(
                        message = message.message,
                        actionMessage = "Dismiss",
                        function = this@MovieDetailsFragment::clearMessage
                    )
                }

                recommendedMoviesAdapter.submitList(uiState.recommendedMovies)
                actorsMoviesAdapter.submitList(uiState.actorList)
            }
        }
    }

    private fun loadValues(binding: FragmentMovieDetailsBinding) {
        binding.toolbar.title = "Detail Screen"


        initRecommendedAdapter()
        initActorsAdapter()

        val str : String = currentMovieId.toString()

//        binding.movieImg
//        binding.wallpaperImg
        binding.titleTxt.text = str
        binding.ratePercentTxt.text = str
        binding.voteAmountTxt.text = str
        binding.statusTitleTxt.text = str
        binding.statusTxt.text = str
        binding.budgetTitleTxt.text = str
        binding.budgetTxt.text = str
        binding.revenueTitleTxt.text = str
        binding.revenueTxt.text = str
        binding.descriptionTitleTxt.text = str
        binding.descriptionTxt.text = str
        binding.recommendationTitleTxt.text = str
        binding.actorsTitleTxt.text = str
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


}