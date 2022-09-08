package com.example.moviestmdb.ui_movies.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Genre
import com.example.moviestmdb.Movie
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.example.moviestmdb.ui_movies.databinding.ListItemPopularMovieBinding
import com.example.moviestmdb.ui_movies.fragments.model.MovieAndGenre
import com.example.moviestmdb.util.TmdbImageUrlProvider
import com.google.android.material.chip.ChipGroup

class PopularMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<MovieAndGenre, PopularMovieViewHolder>(PopularEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        val binding = ListItemPopularMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PopularMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movieAndGenre ->
            holder.binding.popularTitle.text = movieAndGenre.movie.title
            holder.binding.popularSubtitle.text = "${movieAndGenre.movie.voteCount} votes • ${movieAndGenre.movie.releaseDate}"

            movieAndGenre.movie.voteAverage?.let { voteAverage ->
                holder.binding.popularPopularityBadge.progress = (voteAverage * 10).toInt()
            }


            entry.movie.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.popularImage)
            }

            val filteredList = movieAndGenre.genre.filter {
                movieAndGenre.movie.genreList.contains(it.id)
            }
            holder.binding.chipGroup.removeAllViews()
            addChips(holder.binding.chipGroup, filteredList)

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.movie.id)
            }
        }
    }
    fun addChips(chipGroup: ChipGroup, chips: List<Genre>) {
        chips.forEach { genre ->
            val chip = ChipBinding.inflate(LayoutInflater.from(chipGroup.context)).root
            chip.id = genre.id
            chip.text = genre.name
            chip.isCheckable = false
            chipGroup.addView(chip)
        }
    }
}

class PopularMovieViewHolder(
    internal val binding: ListItemPopularMovieBinding
) : RecyclerView.ViewHolder(binding.root)

object PopularEntryComparator : DiffUtil.ItemCallback<MovieAndGenre>() {
    override fun areItemsTheSame(
        oldItem: MovieAndGenre,
        newItem: MovieAndGenre
    ): Boolean {
        return oldItem.movie.id == newItem.movie.id
    }

    override fun areContentsTheSame(
        oldItem: MovieAndGenre,
        newItem: MovieAndGenre
    ): Boolean {
        return oldItem == newItem
    }
}