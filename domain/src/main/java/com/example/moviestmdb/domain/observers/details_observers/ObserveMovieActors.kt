package com.example.moviestmdb.domain.observers.details_observers

import com.example.moviestmdb.Cast
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class ObserveMovieActors @Inject constructor(
    private val moviesRepository: MoviesRepository
) : SubjectInteractor<ObserveMovieActors.Params, List<Cast>>() {

    override fun createObservable(params: Params): Flow<List<Cast>> {
        return moviesRepository.observeMovieActors(movieId = params.movieId)
            .filterNotNull()
    }

    data class Params(val movieId: Int)
}
