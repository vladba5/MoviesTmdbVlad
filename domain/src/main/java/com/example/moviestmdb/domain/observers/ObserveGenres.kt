package com.example.moviestmdb.domain.observers

import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGenres @Inject constructor(
    private val moviesRepository: MoviesRepository
) : SubjectInteractor<Unit, List<Genre>>() {

    override fun createObservable(params: Unit): Flow<List<Genre>> {
        return moviesRepository.observeMovieGenre()
    }
}