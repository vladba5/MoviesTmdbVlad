package com.example.moviestmdb.domain.observers.details_observers

import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.domain.SubjectInteractor
import com.example.moviestmdb.domain.observers.details_observers.ObserveRecommendedMovies.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveRecommendedMovies @Inject constructor(
    private val moviesRepository: MoviesRepository
) : SubjectInteractor<Params, List<Movie>>() {

    override fun createObservable(params: Params): Flow<List<Movie>> {
        return moviesRepository.observeRecommendedMovies()
            .map { list ->  list.flatMap { it.value } }
    }

    data class Params(val movieId: Int)
}