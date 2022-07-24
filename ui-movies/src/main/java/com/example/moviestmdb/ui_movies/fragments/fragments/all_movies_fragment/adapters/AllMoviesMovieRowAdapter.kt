package com.example.moviestmdb.ui_movies.fragments.fragments.all_movies_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core_ui.databinding.AllMoviesRowItemBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider

class AllMoviesMovieRowAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onRowClickListener: (movieId: Int) -> Unit
) : PagingDataAdapter<Movie, AllMoviesMovieRowViewHolder>(AllMoviesMovieDiff) {
//) : ListAdapter<Movie, AllMoviesMovieRowViewHolder>(AllMoviesMovieDiff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllMoviesMovieRowViewHolder {
        val binding = AllMoviesRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return AllMoviesMovieRowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllMoviesMovieRowViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movie ->
            holder.binding.movieTitle.text = movie.title
            holder.binding.movieInfo.text = "${movie.voteCount} votes • ${movie.releaseDate}"

            movie.voteAverage?.let { voteAverage ->
                holder.binding.popularityBadge.progress = (voteAverage * 10).toInt()
            }

            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width
                        )
                    )
                    .into(holder.binding.allMoviesRowImg)
            }


            holder.itemView.setOnClickListener {
                onRowClickListener(entry.id)
            }
        }
    }


}

class AllMoviesMovieRowViewHolder(
    internal val binding: AllMoviesRowItemBinding
) : RecyclerView.ViewHolder(binding.root)

object AllMoviesMovieDiff : DiffUtil.ItemCallback<Movie>() {
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