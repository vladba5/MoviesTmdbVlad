package com.example.moviestmdb.domain.observers

import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.di.NowPlaying
import com.example.moviestmdb.core.di.Popular
import com.example.moviestmdb.core.di.TopRated
import com.example.moviestmdb.core.di.Upcoming
import com.example.moviestmdb.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveFavoriteMovie @Inject constructor(
    @Popular private val popularStore: MoviesStore,
    @TopRated private val topRatedStore: MoviesStore,
    @Upcoming private val upcomingStore: MoviesStore,
    @NowPlaying private val nowPlayingStore: MoviesStore,
    private val moviesRepository: MoviesRepository
) : SubjectInteractor<ObserveFavoriteMovie.Params, List<Movie>>() {

    override fun createObservable(params: Params): Flow<List<Movie>> {
        return combine(
            popularStore.getAllStoreMovies(),
            topRatedStore.getAllStoreMovies(),
            upcomingStore.getAllStoreMovies(),
            nowPlayingStore.getAllStoreMovies(),
            moviesRepository.getFavoriteMovies()
        ) { popularList, topRatedList, upcomingList, nowPlayingList, favoriteMovieIds ->
            val moviesList = mutableListOf<Movie>()
            moviesList.addAll(popularList)
            moviesList.addAll(topRatedList)
            moviesList.addAll(upcomingList)
            moviesList.addAll(nowPlayingList)
            moviesList.filter {
                favoriteMovieIds.contains(it.id)
            }
        }
    }

    data class Params(val page: Int)
}