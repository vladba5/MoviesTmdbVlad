package com.example.moviestmdb.core_ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core_ui.databinding.AllMoviesRowItemBinding
import com.example.moviestmdb.core_ui.databinding.MovieDetailsViewBinding

class DetailMovieView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
    ): ConstraintLayout(context, attrs, defStyleAttr) {

        private val binding =  MovieDetailsViewBinding.inflate(LayoutInflater.from(context), this, true)

    }