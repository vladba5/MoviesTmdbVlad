package com.example.moviestmdb.ui_movies.fragments.fragments.movie_details

import com.example.moviestmdb.Cast
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.util.UiMessage

data class DetailsUiState (
    val recommendedMovies: List<Movie> = emptyList(),
    val recommendedRefreshing: Boolean = false,
    val actorList: List<Cast> = emptyList(),
    val actorsRefreshing: Boolean = false,
    val movie: Movie? = null,
    val message: UiMessage? = null
) {
    val refreshing: Boolean
        get() = recommendedRefreshing || actorsRefreshing

    companion object {
        val Empty = DetailsUiState()
    }
}