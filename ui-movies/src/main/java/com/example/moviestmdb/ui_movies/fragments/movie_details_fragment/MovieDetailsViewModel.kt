package com.example.moviestmdb.ui_movies.fragments.movie_details_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.core.util.ObservableLoadingCounter
import com.example.moviestmdb.core.util.UiMessageManager
import com.example.moviestmdb.core.util.collectStatus
import com.example.moviestmdb.domain.interactors.UpdateMovieActors
import com.example.moviestmdb.domain.interactors.UpdateMovieRecommended
import com.example.moviestmdb.domain.observers.details_observers.ObserveMovieActors
import com.example.moviestmdb.domain.observers.details_observers.ObserveRecommendedMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val updateMovieRecommended: UpdateMovieRecommended,
    private val updateMovieActors: UpdateMovieActors,
    observeRecommendedMovies: ObserveRecommendedMovies,
    observeMovieActors: ObserveMovieActors,
    private val dispatchers: AppCoroutineDispatchers
) : ViewModel() {
    private val recommendedLoadingState = ObservableLoadingCounter()
    private val actorsLoadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val movieId : Int = 555 //will be sent via bundle


    init {
        observeRecommendedMovies(ObserveRecommendedMovies.Params(movieId))
        observeMovieActors(ObserveMovieActors.Params(movieId))

        refresh()
    }

    val detailState: StateFlow<DetailsViewState> = combine(
        recommendedLoadingState.observable,
        actorsLoadingState.observable,
        observeRecommendedMovies.flow,
        observeMovieActors.flow,
        uiMessageManager.message,
    ) { recommendedRefreshing, actorsRefreshing,
        recommendedMovies, movieActors, message ->
        DetailsViewState(
            recommendedMovies = recommendedMovies,
            recommendedRefreshing = recommendedRefreshing,
            actorList = movieActors,
            actorsRefreshing = actorsRefreshing,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailsViewState.Empty
    )

    fun refresh() {
        viewModelScope.launch(dispatchers.io) {
            updateMovieRecommended(UpdateMovieRecommended.Params(movieId))
                .collectStatus(
                    recommendedLoadingState,
                    uiMessageManager
                )
        }

        viewModelScope.launch(dispatchers.io) {
            updateMovieActors(UpdateMovieActors.Params(movieId))
                .collectStatus(
                    actorsLoadingState,
                    uiMessageManager
                )
        }
    }
}