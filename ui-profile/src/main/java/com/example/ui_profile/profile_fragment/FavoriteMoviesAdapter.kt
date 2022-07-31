package com.example.moviestmdb.ui_movies.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.util.TmdbImageUrlProvider
import com.example.ui_profile.databinding.ListItemFavoriteMovieBinding

class FavoriteMoviesAdapterMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<Movie, FavoriteMoviesAdapterMovieViewHolder>(FavoriteMoviesAdapterEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesAdapterMovieViewHolder {
        val binding = ListItemFavoriteMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteMoviesAdapterMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMoviesAdapterMovieViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movie ->
            holder.binding.favoriteTitle.text = movie.title
            holder.binding.favoriteSubtitle.text = "${movie.voteCount} votes • ${movie.releaseDate}"

            movie.voteAverage?.let { voteAverage ->
                holder.binding.favoritePopularityBadge.progress = (voteAverage * 10).toInt()
            }


            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.favoriteImage)
            }

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.id)
            }
        }
    }
}

class FavoriteMoviesAdapterMovieViewHolder(
    internal val binding: ListItemFavoriteMovieBinding
) : RecyclerView.ViewHolder(binding.root)

object FavoriteMoviesAdapterEntryComparator : DiffUtil.ItemCallback<Movie>() {
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