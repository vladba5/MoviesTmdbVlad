package com.example.moviestmdb.domain.observers.details_observers

import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.di.NowPlaying
import com.example.moviestmdb.core.di.Popular
import com.example.moviestmdb.core.di.TopRated
import com.example.moviestmdb.core.di.Upcoming
import com.example.moviestmdb.domain.SubjectInteractor
import com.example.moviestmdb.domain.observers.details_observers.ObserveMovieById.Params
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMovieById @Inject constructor(
    @Popular private val popularStore: MoviesStore,
    @TopRated private val topRatedStore: MoviesStore,
    @Upcoming private val upcomingStore: MoviesStore,
    @NowPlaying private val nowPlayingStore: MoviesStore,
) : SubjectInteractor<Params, Movie>() {

    override fun createObservable(params: Params): Flow<Movie> {
        return combine(
            popularStore.getAllStoreMovies(),
            topRatedStore.getAllStoreMovies(),
            upcomingStore.getAllStoreMovies(),
            nowPlayingStore.getAllStoreMovies()
        ) { popularList, topRatedList, upcomingList, nowPlayingList ->
            val moviesList = mutableListOf<Movie>()
            moviesList.addAll(popularList)
            moviesList.addAll(topRatedList)
            moviesList.addAll(upcomingList)
            moviesList.addAll(nowPlayingList)
            moviesList
        }.map {
            it.first{ movie ->
                movie.id == params.movieId
            }
        }
    }

    data class Params(val movieId: Int)
}
