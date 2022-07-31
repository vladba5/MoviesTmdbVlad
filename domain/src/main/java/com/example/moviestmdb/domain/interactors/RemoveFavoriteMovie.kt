package com.example.moviestmdb.domain.interactors

import com.example.moviestmdb.core.managers.FireBaseManager
import com.example.moviestmdb.domain.ResultInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFavoriteMovie @Inject constructor(
    private val fireBaseManager: FireBaseManager,
) : ResultInteractor<RemoveFavoriteMovie.Params, Flow<Boolean>>() {


    override suspend fun doWork(params: Params): Flow<Boolean> {
        return fireBaseManager.removeFavoriteMovies(params.movieId)
    }

    data class Params(val movieId: Int)

}