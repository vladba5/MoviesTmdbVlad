package com.example.moviestmdb.ui_movies.fragments.fragments.top_rated_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.interactors.UpdateGenres
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.domain.observers.paging_observers.ObservePagedTopRatedMovies
import com.example.moviestmdb.domain.observers.paging_observers.ObservePagedUpcomingMovies
import com.example.moviestmdb.ui_movies.fragments.model.MovieAndGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopRatedMoviesViewModel @Inject constructor(
    pagingInteractor: ObservePagedTopRatedMovies,
    observeGenres: ObserveGenres,
    val updateGenres: UpdateGenres,
    private val dispatchers: AppCoroutineDispatchers
): ViewModel() {
    private val _selectedChips = MutableStateFlow<Set<Int>>(emptySet())
    private val selectedChips = _selectedChips.asStateFlow()

    private val filteresAndGenresCombo = combine(
        selectedChips,
        observeGenres.flow
    ) { selectedChips, genresList ->
        Pair(selectedChips, genresList)
    }

    val pageList: Flow<PagingData<MovieAndGenre>> =
        filteresAndGenresCombo.flatMapLatest { pair ->
            pagingInteractor.flow
                .map { pagingData ->
                    if (pair.first.isEmpty()) {
                        pagingData
                    } else {
                        pagingData.filter {
                            it.genreList.any { id ->
                                id in pair.first
                            }
                        }
                    }
                }.map { pagingData ->
                    pagingData.map { movie ->
                        MovieAndGenre(
                            movie,
                            pair.second
                        )
                    }
                }
        }.cachedIn(viewModelScope)

    val filteredChips = observeGenres.flow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun toggleFilter(id: Int, enabled: Boolean) {
        val set = _selectedChips.value.toMutableSet()
        val changed =
            if (enabled) {
                set.add(id)
            } else {
                set.remove(id)
            }

        if (changed) {
//            _selectedChips.value = set
            _selectedChips.tryEmit(set)
        }
    }

    private val PAGING_CONFIG = PagingConfig(
        pageSize = 20,
        initialLoadSize = 40,
        maxSize = 1000
    )

    init {
        pagingInteractor(ObservePagedTopRatedMovies.Params(PAGING_CONFIG))
        observeGenres(Unit)

        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(dispatchers.io) {
            updateGenres(Unit).collect()
        }
    }

}