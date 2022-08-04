package com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.observers.ObserveGenres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FilterBottomSheetViewModel @Inject constructor(
    observeGenres: ObserveGenres,
    val dispatchers: AppCoroutineDispatchers
) : ViewModel() {

    val geners = observeGenres.flow
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        observeGenres(Unit)
    }
}