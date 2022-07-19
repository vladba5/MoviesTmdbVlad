package com.example.moviestmdb.ui_movies.lobby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core_ui.databinding.MovieCardBinding
import com.example.moviestmdb.ui_movies.databinding.ActorItemRowBinding
import com.example.moviestmdb.ui_movies.databinding.RecommendedItemRowBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider

class ActorsMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider
) : ListAdapter<Movie, ActorsMoviesViewHolder>(ActorsMoviesDiff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActorsMoviesViewHolder {
        val binding = ActorItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ActorsMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorsMoviesViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let {
            holder.binding.actorTitle.text = it.title

            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width
                        )
                    )
                    .into(holder.binding.actorImg)
            }
        }
    }
}

class ActorsMoviesViewHolder(
    internal val binding: ActorItemRowBinding
) : RecyclerView.ViewHolder(binding.root)

object ActorsMoviesDiff : DiffUtil.ItemCallback<Movie>() {
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