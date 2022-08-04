package com.example.moviestmdb.ui_movies.popular

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.core.util.ObservableLoadingCounter
import com.example.moviestmdb.core.util.UiMessageManager
import com.example.moviestmdb.domain.interactors.UpdateGenres
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.domain.observers.paging_observers.ObservePagedPopularMovies
import com.example.moviestmdb.ui_movies.fragments.fragments.lobby.LobbyViewState
import com.example.moviestmdb.ui_movies.fragments.fragments.popular_movies.PopularViewState
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    observePagedPopularMovies: ObservePagedPopularMovies,
    observeGenres: ObserveGenres,
    val updateGenres: UpdateGenres,
    private val dispatchers: AppCoroutineDispatchers
) : ViewModel() {

    private val uiMessageManager = UiMessageManager()
    private val popularLoadingState = ObservableLoadingCounter()

    private val PAGING_CONFIG = PagingConfig(
        pageSize = 20,
        initialLoadSize = 40,
        maxSize = 1000
    )

    val pagedList: Flow<PagingData<Movie>> =
        observePagedPopularMovies.flow.cachedIn(viewModelScope)



//    val state: StateFlow<PopularViewState> = combine(
//        observePagedPopularMovies.flow,
//        observeGenres.flow,
//        popularLoadingState.observable,
//        uiMessageManager.message,
//    ) { observePagedPopularMovies, genresList, popularRefreshing, message ->
//        PopularViewState(
//            popularPagingData = observePagedPopularMovies,
//            genreList = genresList,
//            popularRefreshing = popularRefreshing,
//            message = message,
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        PopularViewState.Empty
//    )



    init {
        observePagedPopularMovies(ObservePagedPopularMovies.Params(PAGING_CONFIG))
        observeGenres(Unit)

        fetchData()
    }


    private fun fetchData() {
        viewModelScope.launch(dispatchers.io) {
            updateGenres(Unit).collect()
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}