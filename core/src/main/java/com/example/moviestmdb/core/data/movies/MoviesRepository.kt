package com.example.moviestmdb.core.data.movies

import com.example.moviestmdb.Cast
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.datasources.MoviesLocalDataSource
import com.example.moviestmdb.core.data.movies.datasources.MoviesRemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val remote: MoviesRemoteDataSource,
    private val local: MoviesLocalDataSource
) {
    //region popular movies
    suspend fun getPopularMovies(page: Int) =
        flow {
            emit(remote.getPopularMovies(page))
        }

    fun savePopularMovies(page: Int, movies: List<Movie>) {
        local.popularStore.insert(page, movies)
    }

    fun observePopularMovies() = local.popularStore.observeMovies()
    //endregion

    //region Now Playing movies
    suspend fun getNowPlayingMovies(page: Int) =
        flow {
            emit(remote.getNowPlayingMovies(page))
        }

    fun saveNowPlayingMovies(page: Int, movies: List<Movie>) {
        local.nowPlayingStore.insert(page, movies)
    }

    fun observeNowPlayingMovies() = local.nowPlayingStore.observeMovies()
    //endregion

    //region Top Rated movies
    suspend fun getTopRatedMovies(page: Int) =
        flow {
            emit(remote.getTopRated(page))
        }

    fun saveTopRatedMovies(page: Int, movies: List<Movie>) {
        local.topRatedStore.insert(page, movies)
    }

    fun observeTopRatedMovies() = local.topRatedStore.observeMovies()
    //endregion

    //region Upcoming movies
    suspend fun getUpcomingMovies(page: Int) =
        flow {
            emit(remote.getUpcoming(page))
        }

    fun saveUpcomingMovies(page: Int, movies: List<Movie>) {
        local.upcomingStore.insert(page, movies)
    }

    fun observeUpcomingMovies() = local.upcomingStore.observeMovies()
    //endregion

    //region Recommended movies
    suspend fun getRecommended(movieId: Int) =
        flow {
            emit(remote.getRecommended(movieId))
        }

    fun saveMovieRecommended(movieId: Int, movies: List<Movie>) {
        local.movieStore.insertRecommended(movieId, movies)

    }

    fun observeRecommendedMovies() = local.movieStore.observeRecommendedMovies()
    //endregion

    //region Actors
    suspend fun getActors(movieId: Int) =
        flow {
            emit(remote.getCredits(movieId = movieId))
        }

    fun saveMovieActors(movieId: Int, actors: List<Cast>) {
        local.movieStore.insertActors(movieId, actors)
    }

    fun observeActors() = local.movieStore.observeAssociatedActors()
    //endregion




    fun observeMovieRecommended(movieId: Int) = local.upcomingStore.getRecommendedForMovie(movieId)
    fun observeMovieActors(movieId: Int) = local.upcomingStore.getActorsForMovie(movieId)

//    fun observeUpcomingRecommendedMovies() = local.upcomingStore.observeRecommendedMovies()
//    fun observeUpcomingActorsMovies() = local.upcomingStore.observeAssociatedActors()
//
//    fun observeTopRatedRecommendedMovies() = local.topRatedStore.observeRecommendedMovies()
//    fun observeTopRatedActorsMovies() = local.topRatedStore.observeAssociatedActors()
//
//    fun observeNowPlayingRecommendedMovies() = local.nowPlayingStore.observeRecommendedMovies()
//    fun observeNowPlayingActorsMovies() = local.nowPlayingStore.observeAssociatedActors()
//
//    fun observePopularRecommendedMovies() = local.popularStore.observeRecommendedMovies()
//    fun observePopularActorsMovies() = local.popularStore.observeAssociatedActors()
}