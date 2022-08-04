package com.example.moviestmdb.core.data.movies

import com.example.moviestmdb.Cast
import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.datasources.MoviesLocalDataSource
import com.example.moviestmdb.core.data.movies.datasources.MoviesRemoteDataSource
import com.example.moviestmdb.core.managers.FireBaseManager
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val remote: MoviesRemoteDataSource,
    private val local: MoviesLocalDataSource,
    val fireBaseManager: FireBaseManager
) {

    fun getFavoriteMovies() =
        fireBaseManager.observeFavoritesMovies()

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
        Timber.d("1 vlad saveMovieRecommended id $movieId and size is ${movies.size} ")
        local.detailStore.insertRecommended(movieId, movies)
    }

    fun observeMovieRecommended(movieId: Int) = local.detailStore.getRecommendedForMovie(movieId)
    //endregion

    //region Actors
    suspend fun getActors(movieId: Int) =
        flow {
            emit(remote.getCredits(movieId = movieId))
        }

    fun saveMovieActors(movieId: Int, actors: List<Cast>) {
        local.detailStore.insertActors(movieId, actors)
    }

    fun observeMovieActors(movieId: Int) = local.detailStore.getActorsForMovie(movieId)
    //endregion

    //region Genre
    suspend fun getGenres() =
        flow {
            emit(remote.getGenres())
        }

    fun saveGenres(genres: List<Genre>) {
        local.insertGenre(genres)
    }

    fun observeMovieGenre() = local.observeGenre()
    //endregion


    suspend fun getFilteredMovieList(page: Int ,request: Map<String,String>) =
        flow{
            emit(remote.getDiscover(page ,request))
        }
}