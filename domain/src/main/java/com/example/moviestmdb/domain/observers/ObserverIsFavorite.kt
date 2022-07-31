package com.example.moviestmdb.domain.observers

import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.di.NowPlaying
import com.example.moviestmdb.core.di.Popular
import com.example.moviestmdb.core.di.TopRated
import com.example.moviestmdb.core.di.Upcoming
import com.example.moviestmdb.core.managers.FireBaseManager
import com.example.moviestmdb.domain.SubjectInteractor
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ObserverIsFavorite @Inject constructor(
    @Popular private val popularStore: MoviesStore,
    @TopRated private val topRatedStore: MoviesStore,
    @Upcoming private val upcomingStore: MoviesStore,
    @NowPlaying private val nowPlayingStore: MoviesStore,
    val fireBaseManager: FireBaseManager
) : SubjectInteractor<ObserverIsFavorite.Params, Boolean>() {

    override fun createObservable(params: ObserverIsFavorite.Params): Flow<Boolean> {
        return combine(
            popularStore.getAllStoreMovies(),
            topRatedStore.getAllStoreMovies(),
            upcomingStore.getAllStoreMovies(),
            nowPlayingStore.getAllStoreMovies(),
            fireBaseManager.observeFavoritesMovies()
        ) { popularList, topRatedList, upcomingList, nowPlayingList, favoriteMovies ->
            val moviesList = mutableListOf<Movie>()
            moviesList.addAll(popularList)
            moviesList.addAll(topRatedList)
            moviesList.addAll(upcomingList)
            moviesList.addAll(nowPlayingList)
            moviesList
        }.map {
            it.firstOrNull {
                it.id == params.movieId
            }
        }.map { favoriteMovie ->
            favoriteMovie == null
        }
    }

    data class Params(val movieId: Int)
}