package com.example.moviestmdb.core.data.movies

import com.example.moviestmdb.Cast
import com.example.moviestmdb.Movie
import com.example.moviestmdb.MovieCredit
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsStore @Inject constructor(){
    private val _associatedActors = MutableSharedFlow<Map<Int, List<Cast>>>(replay = 1)
    private val _recommendedMovies = MutableSharedFlow<Map<Int, List<Movie>>>(replay = 1)

    fun observeRecommendedMovies(): SharedFlow<Map<Int, List<Movie>>> = _recommendedMovies.asSharedFlow()
    fun observeAssociatedActors(): SharedFlow<Map<Int, List<Cast>>> = _associatedActors.asSharedFlow()

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

    fun deleteAll() {
        _associatedActors.resetReplayCache()
        _recommendedMovies.resetReplayCache()
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

//    fun getRecommendedForMovie2(movieId: Int): Flow<List<Movie>> {
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
}