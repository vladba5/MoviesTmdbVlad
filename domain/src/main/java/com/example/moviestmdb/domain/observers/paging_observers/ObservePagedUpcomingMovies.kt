package com.example.moviestmdb.domain.observers.paging_observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.di.Upcoming
import com.example.moviestmdb.domain.InvalidatingPagingSourceFactory
import com.example.moviestmdb.domain.PaginatedMovieRemoteMediator
import com.example.moviestmdb.domain.PagingInteractor
import com.example.moviestmdb.domain.interactors.UpdateUpcomingMovies
import com.example.moviestmdb.domain.pagingSources.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ObservePagedUpcomingMovies @Inject constructor(
    @Upcoming private val upcomingStore: MoviesStore,
    private val updateUpcomingMovies: UpdateUpcomingMovies,
) : PagingInteractor<ObservePagedUpcomingMovies.Params, Movie>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedMovieRemoteMediator(moviesStore = upcomingStore) { page ->
                updateUpcomingMovies.executeSync(UpdateUpcomingMovies.Params(page))
                pagingSourceFactory.invalidate()
            },
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory(::createPagingSource)

    private fun createPagingSource(): MoviesPagingSource {
        return MoviesPagingSource(upcomingStore)
    }

    data class Params(
        override val pagingConfig: PagingConfig,
    ) : Parameters<Movie>


}