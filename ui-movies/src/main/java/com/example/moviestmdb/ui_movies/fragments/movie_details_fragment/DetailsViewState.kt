package com.example.moviestmdb.ui_movies.fragments.movie_details_fragment

import com.example.moviestmdb.Cast
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.util.UiMessage

data class DetailsViewState (
    val recommendedMovies: List<Movie> = emptyList(),
    val recommendedRefreshing: Boolean = false,
    val actorList: List<Cast> = emptyList(),
    val actorsRefreshing: Boolean = false,
    val message: UiMessage? = null
) {
    val refreshing: Boolean
        get() = recommendedRefreshing || actorsRefreshing

    companion object {
        val Empty = DetailsViewState()
    }
}