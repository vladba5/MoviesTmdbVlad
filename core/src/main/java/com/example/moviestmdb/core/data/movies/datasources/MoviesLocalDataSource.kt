package com.example.moviestmdb.core.data.movies.datasources

import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.data.movies.DetailsStore
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.di.NowPlaying
import com.example.moviestmdb.core.di.Popular
import com.example.moviestmdb.core.di.TopRated
import com.example.moviestmdb.core.di.Upcoming
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesLocalDataSource @Inject constructor(
    @Popular val popularStore: MoviesStore,
    @TopRated val topRatedStore: MoviesStore,
    @Upcoming val upcomingStore: MoviesStore,
    @NowPlaying val nowPlayingStore: MoviesStore,
    val detailStore : DetailsStore
) {
    private val _genreList = MutableSharedFlow<List<Genre>>(replay = 1).apply {
        tryEmit(emptyList())
    }

    fun insertGenre(genres: List<Genre>) {
        _genreList.tryEmit(genres)
    }

    fun observeGenre(): SharedFlow<List<Genre>> = _genreList.asSharedFlow()
}