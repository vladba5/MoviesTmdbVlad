package com.example.moviestmdb.ui_movies.fragments.fragments.popular_movies

import androidx.paging.PagingData
import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.util.UiMessage

class PopularViewState (
    val popularPagingData: PagingData<Movie> = PagingData.empty(),
    val popularRefreshing: Boolean = false,
    val genreList: List<Genre> = emptyList(),
    val message: UiMessage? = null
) {
    val refreshing: Boolean
        get() = popularRefreshing

    companion object {
        val Empty = PopularViewState()
    }
}