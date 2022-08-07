package com.example.moviestmdb.ui_movies.fragments.fragments.discover_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.moviestmdb.Movie
//import com.example.moviestmdb.domain.InvalidatingPagingSourceFactory
import com.example.moviestmdb.domain.interactors.UpdateDiscoverMovies
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.domain.pagingSources.DiscoverPagingSource
import com.example.moviestmdb.domain.pagingSources.MoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
//    private val updateDiscoverMovies: UpdateDiscoverMovies,
    private val observeDiscoverPagingSource: DiscoverPagingSource,
    observeGenres: ObserveGenres,
) : ViewModel() {

    fun getPagingData(): Flow<PagingData<Movie>> {
        return Pager (config = PAGING_CONFIG,
            pagingSourceFactory = {observeDiscoverPagingSource}
        ).flow.cachedIn(viewModelScope)
    }

//    private val pagingSourceFactory = InvalidatingPagingSourceFactory(::createPagingSource)
//
//    private fun createPagingSource(): DiscoverPagingSource {
//        return DiscoverPagingSource(emptyMap<String,String>())
//    }

    val genres = observeGenres.flow
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val PAGING_CONFIG = PagingConfig(
        pageSize = 20,
        initialLoadSize = 40,
        maxSize = 1000
    )

    init {
        observeGenres(Unit)
    }



}