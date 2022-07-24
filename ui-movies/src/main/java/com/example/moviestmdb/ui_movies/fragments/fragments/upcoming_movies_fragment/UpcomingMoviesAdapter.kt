package com.example.moviestmdb.ui_movies.fragments.fragments.upcoming_movies_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.ui_movies.databinding.ListItemUpcomingMovieBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider

class UpcomingMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<Movie, UpcomingMovieViewHolder>(UpcomingEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMovieViewHolder {
        val binding = ListItemUpcomingMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UpcomingMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingMovieViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movie ->
            holder.binding.upcomingTitle.text = movie.title
            holder.binding.upcomingSubtitle.text = "${movie.voteCount} votes • ${movie.releaseDate}"

            movie.voteAverage?.let { voteAverage ->
                holder.binding.upcomingPopularityBadge.progress = (voteAverage * 10).toInt()
            }


            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.upcomingImage)
            }

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.id)
            }
        }
    }
}

class UpcomingMovieViewHolder(
    internal val binding: ListItemUpcomingMovieBinding
) : RecyclerView.ViewHolder(binding.root)

object UpcomingEntryComparator : DiffUtil.ItemCallback<Movie>() {
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