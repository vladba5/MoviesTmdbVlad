package com.example.moviestmdb.ui_movies.fragments.fragments.top_rated_movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.ui_movies.databinding.ListItemTopRatedMovieBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider

class TopRatedMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<Movie, TopRatedMovieViewHolder>(TopRatedEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedMovieViewHolder {
        val binding = ListItemTopRatedMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TopRatedMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopRatedMovieViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movie ->
            holder.binding.topRatedTitle.text = movie.title
            holder.binding.topRatedSubtitle.text = "${movie.voteCount} votes • ${movie.releaseDate}"

            movie.voteAverage?.let { voteAverage ->
                holder.binding.topRatedPopularityBadge.progress = (voteAverage * 10).toInt()
            }


            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.topRatedImage)
            }

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.id)
            }
        }
    }
}

class TopRatedMovieViewHolder(
    internal val binding: ListItemTopRatedMovieBinding
) : RecyclerView.ViewHolder(binding.root)

object TopRatedEntryComparator : DiffUtil.ItemCallback<Movie>() {
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