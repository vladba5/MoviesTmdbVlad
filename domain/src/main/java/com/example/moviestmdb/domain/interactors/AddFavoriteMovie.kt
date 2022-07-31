package com.example.moviestmdb.domain.interactors

import com.example.moviestmdb.core.managers.FireBaseManager
import com.example.moviestmdb.domain.ResultInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddFavoriteMovie @Inject constructor(
    private val fireBaseManager: FireBaseManager,
) : ResultInteractor<AddFavoriteMovie.Params, Flow<Boolean>>() {


    override suspend fun doWork(params: Params): Flow<Boolean> {
        return fireBaseManager.insertFavoriteMovies(params.movieId)
    }

    data class Params(val movieId: Int)
}