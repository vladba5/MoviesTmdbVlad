package com.example.moviestmdb.ui_movies.lobby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.ui_movies.databinding.RecommendedItemRowBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider

class RecommendedMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider
) : ListAdapter<Movie, RecommendedMoviesViewHolder>(RecommendedMoviesDiff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendedMoviesViewHolder {
        val binding = RecommendedItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecommendedMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendedMoviesViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let {
            holder.binding.recommendedTitle.text = it.title

            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width
                        )
                    )
                    .into(holder.binding.recommendedImg)
            }
        }
    }
}

class RecommendedMoviesViewHolder(
    internal val binding: RecommendedItemRowBinding
) : RecyclerView.ViewHolder(binding.root)

object RecommendedMoviesDiff : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }
}