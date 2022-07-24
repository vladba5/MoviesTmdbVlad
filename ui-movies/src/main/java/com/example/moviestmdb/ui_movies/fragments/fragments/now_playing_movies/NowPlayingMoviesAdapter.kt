package com.example.moviestmdb.ui_movies.fragments.fragments.now_playing_movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.ui_movies.databinding.ListItemNowPlayingMovieBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider

class NowPlayingMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<Movie, NowPlayingMovieViewHolder>(NowPlayingEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingMovieViewHolder {
        val binding = ListItemNowPlayingMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NowPlayingMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NowPlayingMovieViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movie ->
            holder.binding.nowPlayingTitle.text = movie.title
            holder.binding.nowPlayingSubtitle.text = "${movie.voteCount} votes • ${movie.releaseDate}"

            movie.voteAverage?.let { voteAverage ->
                holder.binding.nowPlayingPopularityBadge.progress = (voteAverage * 10).toInt()
            }


            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.nowPlayingImage)
            }

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.id)
            }
        }
    }
}

class NowPlayingMovieViewHolder(
    internal val binding: ListItemNowPlayingMovieBinding
) : RecyclerView.ViewHolder(binding.root)

object NowPlayingEntryComparator : DiffUtil.ItemCallback<Movie>() {
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