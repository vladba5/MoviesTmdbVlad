package com.example.moviestmdb.domain.observers.paging_observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.di.NowPlaying
import com.example.moviestmdb.domain.InvalidatingPagingSourceFactory
import com.example.moviestmdb.domain.PaginatedMovieRemoteMediator
import com.example.moviestmdb.domain.PagingInteractor
import com.example.moviestmdb.domain.interactors.UpdateNowPlayingMovies
import com.example.moviestmdb.domain.pagingSources.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ObservePagedNowPlayingMovies @Inject constructor(
    @NowPlaying private val nowPlayingStore: MoviesStore,
    private val updateNowPlayingMovies: UpdateNowPlayingMovies,
) : PagingInteractor<ObservePagedNowPlayingMovies.Params, Movie>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedMovieRemoteMediator(moviesStore = nowPlayingStore) { page ->
                updateNowPlayingMovies.executeSync(UpdateNowPlayingMovies.Params(page))
                pagingSourceFactory.invalidate()
            },
            pagingSourceFactory = pagingSourceFactory
        ).flow
//            .map {
//
//        }
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory(::createPagingSource)

    private fun createPagingSource(): MoviesPagingSource {
        return MoviesPagingSource(nowPlayingStore)
    }

    data class Params(
        override val pagingConfig: PagingConfig,
    ) : Parameters<Movie>
}