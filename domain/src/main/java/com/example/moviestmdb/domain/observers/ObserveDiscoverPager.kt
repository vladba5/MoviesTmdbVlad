package com.example.moviestmdb.domain.observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.InvalidatingPagingSourceFactory
import com.example.moviestmdb.domain.PaginatedMovieRemoteMediator
import com.example.moviestmdb.domain.PagingInteractor
import com.example.moviestmdb.domain.interactors.UpdateUpcomingMovies
import com.example.moviestmdb.domain.observers.paging_observers.ObservePagedUpcomingMovies
import com.example.moviestmdb.domain.pagingSources.DiscoverPagingSource
import com.example.moviestmdb.domain.pagingSources.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ObserveDiscoverPager @Inject constructor(
    private val moviesRepository: MoviesRepository,
    val dispatchers: AppCoroutineDispatchers,
) : PagingInteractor<ObserveDiscoverPager.Params, Movie>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Movie>> {
        return params.requestMap.flatMapLatest { filters ->
            Pager(
                config = params.pagingConfig,
                pagingSourceFactory = {
                    DiscoverPagingSource(
                        movieRepository = moviesRepository,
                        dispatchers = dispatchers,
                        filters = filters
                    )
                }
            ).flow
        }
    }

//    private val pagingSourceFactory = InvalidatingPagingSourceFactory(::createPagingSource)
//
//    private fun createPagingSource(): DiscoverPagingSource {
//        return DiscoverPagingSource(moviesRepository, dispatchers, requestMap)
//    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val requestMap: Flow<Map<String, String>>
    ) : Parameters<Movie>

}