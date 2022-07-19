package com.example.moviestmdb.domain.interactors

import com.example.moviestmdb.MovieResponse
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.core.result.Result
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.FlowInteractor
import com.example.moviestmdb.domain.interactors.UpdateMovieRecommended.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class UpdateMovieRecommended @Inject constructor(
    private val moviesRepository: MoviesRepository,
    dispatchers: AppCoroutineDispatchers,
) : FlowInteractor<Params, MovieResponse>(dispatchers.io) {

    override suspend fun doWork(params: Params): Flow<Result<MovieResponse>> {
        val movId = when {
            params.movieId >= 1 -> params.movieId
            else -> 1
        }

        return moviesRepository.getRecommended(movId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> Timber.e(result.exception)
                    is Result.Success -> moviesRepository.saveMovieRecommended(
                        movieId = movId,
                        movies = result.data.movieList
                    )
                }
            }
    }

    data class Params(val movieId: Int)
}