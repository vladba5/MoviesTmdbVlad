package com.example.moviestmdb.core.data.movies

import com.example.moviestmdb.Cast
import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie
import com.example.moviestmdb.MovieCredit
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsStore @Inject constructor(){
    private val _associatedActors = MutableSharedFlow<Map<Int, List<Cast>>>(replay = 1).apply {
        tryEmit(emptyMap())
    }
    private val _recommendedMovies = MutableSharedFlow<Map<Int, List<Movie>>>(replay = 1).apply {
        tryEmit(emptyMap())
    }

    fun insertRecommended(movieId: Int, movies: List<Movie>) {
        val map = _recommendedMovies.replayCache.first().toMutableMap()
        map[movieId] = movies
        _recommendedMovies.tryEmit(map)
//        _recommendedMovies.tryEmit(mapOf(movieId to movies))
    }

    fun insertActors(movieId: Int, actors: List<Cast>) {
        val map = _associatedActors.replayCache.first().toMutableMap()
        map[movieId] = actors
        _associatedActors.tryEmit(map)
//        _associatedActors.tryEmit(mapOf(movieId to actors))
    }

    fun deleteAll() {
        _associatedActors.resetReplayCache()
        _recommendedMovies.resetReplayCache()
    }

    fun getRecommendedForMovie(movieId: Int): Flow<List<Movie>?> {
        return _recommendedMovies.map {
            it[movieId]
        }
    }

    fun getActorsForMovie(movieId: Int): Flow<List<Cast>?> {
        return _associatedActors.map {
            it[movieId]
        }
    }

 fun observeRecommendedMovies(): SharedFlow<Map<Int, List<Movie>>> = _recommendedMovies.asSharedFlow()
 fun observeAssociatedActors(): SharedFlow<Map<Int, List<Cast>>> = _associatedActors.asSharedFlow()


//    fun getRecommendedForMovie(movieId: Int): Flow<List<Movie>> {
//        return _recommendedMovies.map {
//                list ->
//            list.flatMap {
//            it.value
//                }
//        }
//    }

//    fun getMovieById(movieId: Int): Flow<Movie> {
//         return _movies.map { it.values.flatten() }
//             .map { list -> list.find { it.id == movieId } }
//             .filterNotNull()
//    }

//    fun getRecommendedForMovie(movieId: Int): Flow<Movie> {
//        return _recommendedMovies.map { it.values.flatten() }
//            .map { list -> list.find { it.id == movieId } }
//            .filterNotNull()
//    }
}