package com.example.moviestmdb.domain.observers.details_observers

import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.domain.SubjectInteractor
import com.example.moviestmdb.domain.observers.details_observers.ObserveRecommendedMovies.Params
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class ObserveRecommendedMovies @Inject constructor(
    private val moviesRepository: MoviesRepository
) : SubjectInteractor<Params, List<Movie>>() {

    override fun createObservable(params: Params): Flow<List<Movie>> {
        return moviesRepository.observeMovieRecommended(movieId = params.movieId)
            .filterNotNull()
    }

    data class Params(val movieId: Int)
}

