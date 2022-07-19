package com.example.moviestmdb.core.data.movies

import com.example.moviestmdb.Cast
import com.example.moviestmdb.MovieCredit
import com.example.moviestmdb.Movie
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesStore @Inject constructor() {
    private val _movies = MutableSharedFlow<Map<Int, List<Movie>>>(replay = 1)
    private val _associatedActors = MutableSharedFlow<Map<Int, List<Cast>>>(replay = 1) //movieId, actors
    private val _recommendedMovies = MutableSharedFlow<Map<Int, List<Movie>>>(replay = 1)//movieId, recommended

    fun insert(page: Int, movies: List<Movie>) {
        if (page == 1) {
            _movies.resetReplayCache()
            _movies.tryEmit(mapOf(page to movies))
        } else {
            val map = _movies.replayCache.first().toMutableMap()
            map[page] = movies
            _movies.tryEmit(map)
        }
    }

    fun insertRecommended(movieId: Int, movies: List<Movie>) {
            val map = _recommendedMovies.replayCache.first().toMutableMap()
            map[movieId] = movies
            _recommendedMovies.tryEmit(map)
    }

    fun insertActors(movieId: Int, movies: List<Cast>) {
        val map = _associatedActors.replayCache.first().toMutableMap()
        map[movieId] = movies
        _associatedActors.tryEmit(map)
    }

    fun observeMovies(): SharedFlow<Map<Int, List<Movie>>> = _movies.asSharedFlow()
    fun observeRecommendedMovies(): SharedFlow<Map<Int, List<Movie>>> = _recommendedMovies.asSharedFlow()
    fun observeAssociatedActors(): SharedFlow<Map<Int, List<Cast>>> = _associatedActors.asSharedFlow()

    fun updatePage(page: Int, movies: List<Movie>) {
        val map = _movies.replayCache.first().toMutableMap()
        map[page] = movies
        _movies.tryEmit(map)
    }

    fun deletePage(page: Int) {
        val map = _movies.replayCache.first().toMutableMap()
        map.remove(page)
        _movies.tryEmit(map)
    }

    fun deleteAll() {
        _movies.resetReplayCache()
        _associatedActors.resetReplayCache()
        _recommendedMovies.resetReplayCache()
    }

    fun getLastPage(): Int {
        return _movies.replayCache.firstOrNull()?.let { map ->
            map.maxOf { it.key }
        } ?: 0
    }

    fun getMoviesForPage(page: Int): List<Movie>? {
        return _movies.replayCache.firstOrNull()?.let {
            it[page]
        }
    }

    fun getRecommendedForMovie(movieId: Int): Flow<Movie> {
        return _recommendedMovies.map { it.values.flatten() }
            .map { list -> list.find { it.id == movieId } }
            .filterNotNull()
    }

    fun getActorsForMovie(movieId: Int): Flow<Cast> {
        return _associatedActors.map { it.values.flatten() }
            .map { list -> list.find { it.id == movieId } }
            .filterNotNull()
    }

//    fun getMovieById(movieId: Int): Flow<Movie> {
//         return _movies.map { it.values.flatten() }
//             .map { list -> list.find { it.id == movieId } }
//             .filterNotNull()
//    }

}