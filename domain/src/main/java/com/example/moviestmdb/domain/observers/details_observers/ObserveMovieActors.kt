package com.example.moviestmdb.domain.observers.details_observers

import com.example.moviestmdb.Cast
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.domain.SubjectInteractor
import com.example.moviestmdb.domain.observers.details_observers.ObserveMovieActors.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMovieActors @Inject constructor(
    private val moviesRepository: MoviesRepository
) : SubjectInteractor<Params, List<Cast>>() {

    override fun createObservable(params: Params): Flow<List<Cast>> {
        return moviesRepository.observeActors()
            .map { list ->
                list.flatMap {
                    it.value
                }
            }
    }

    data class Params(val movieId: Int)
}