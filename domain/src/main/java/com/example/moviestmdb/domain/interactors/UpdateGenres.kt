package com.example.moviestmdb.domain.interactors

import com.example.moviestmdb.Genre
import com.example.moviestmdb.GenreResponse
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.core.result.Result
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.FlowInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class UpdateGenres  @Inject constructor(
    private val moviesRepository: MoviesRepository,
    dispatchers: AppCoroutineDispatchers,
) : FlowInteractor<Unit, GenreResponse>(dispatchers.io) {

    override suspend fun doWork(params: Unit): Flow<Result<GenreResponse>> {
        return moviesRepository.getGenres()
            .onEach { result ->
                when (result) {
                    is Result.Error -> Timber.e(result.exception)
                    is Result.Success -> moviesRepository.saveGenres(result.data.genres)
                }
            }
    }
}