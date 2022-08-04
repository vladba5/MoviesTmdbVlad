package com.example.moviestmdb.ui_movies.fragments.view_holder

import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie

data class MovieAndGenre(
    val movie: Movie,
    val genre: List<Genre>
)