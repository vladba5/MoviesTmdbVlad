package com.example.moviestmdb.ui_movies.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviestmdb.Movie
import com.example.moviestmdb.domain.observers.paging_observers.ObservePagedPopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    pagingInteractor: ObservePagedPopularMovies
): ViewModel() {

    val pagedList: Flow<PagingData<Movie>> =
        pagingInteractor.flow.cachedIn(viewModelScope)


    private val PAGING_CONFIG = PagingConfig(
        pageSize = 20,
        initialLoadSize = 40,
        maxSize = 1000
    )

    init {
        pagingInteractor(ObservePagedPopularMovies.Params(PAGING_CONFIG))
    }

}