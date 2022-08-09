package com.example.moviestmdb.ui_movies.fragments.fragments.discover_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.moviestmdb.Movie
import com.example.moviestmdb.domain.PagingInteractor
//import com.example.moviestmdb.domain.InvalidatingPagingSourceFactory
import com.example.moviestmdb.domain.interactors.UpdateDiscoverMovies
import com.example.moviestmdb.domain.observers.ObserveDiscoverPager
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.domain.pagingSources.DiscoverPagingSource
import com.example.moviestmdb.domain.pagingSources.MoviesPagingSource
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    //private val observeDiscoverPagingSource: DiscoverPagingSource,
    observeDiscoverPager: ObserveDiscoverPager,
    observeGenres: ObserveGenres,
) : ViewModel() {

    private val _selectedFilters = MutableStateFlow<Map<String, String>>(emptyMap())
    val selectedFilters = _selectedFilters.asStateFlow()

    init {
        observeGenres(Unit)

//        viewModelScope.launch {
//            _selectedFilters.collect {
//                Timber.i("### ${it}")
//            }
//        }

        observeDiscoverPager(
            params = ObserveDiscoverPager.Params(
                PAGING_CONFIG,
                _selectedFilters.asStateFlow()
            )
        )

        viewModelScope.launch {
            _selectedFilters.tryEmit(emptyMap())
        }
    }

    val pagedList: Flow<PagingData<Movie>> =
        observeDiscoverPager.flow.cachedIn(viewModelScope)

//    fun newFilters(filters: Map<String, String>){
//        observeDiscoverPager(
//            params = ObserveDiscoverPager.Params(
//                PAGING_CONFIG,
//                filters
//            )
//        )
//    }

    fun replaceFilters(filterParam: FilterParams){
        viewModelScope.launch {
            _selectedFilters.tryEmit(filterParam.toMap())
        }
    }

    val genres = observeGenres.flow
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            maxSize = 1000
        )
    }
}