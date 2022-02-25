package com.khs.movies.repository

import androidx.lifecycle.MutableLiveData
import com.khs.movies.R
import com.khs.movies.api.ApiService
import com.khs.movies.model.dto.Movie001
import com.khs.movies.model.dto.MovieResponse
import com.khs.movies.model.common.*
import com.khs.movies.persistance.AppDatabase
import com.khs.movies.persistance.dao.PopularMovieDao
import com.khs.movies.persistance.dao.UpcomingMovieDao
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.persistance.entitiy.UpcomingMovie
import com.khs.movies.ui.main.state.MainViewState
import com.khs.movies.ui.main.state.MoviesField
import com.khs.movies.util.manager.NetworkBoundResource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class MainRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : MainRepository {
    override suspend fun getPopularMovies(
        stateEvent: StateEvent,
        isConnected: Boolean,
        isFromCache: Boolean,
        apiErrorCallback: TwoActionDialogCallback
    ): Flow<DataState<MainViewState>> =
        object : NetworkBoundResource<MovieResponse, List<PopularMovie>, MainViewState>(
            dispatcher = ioDispatcher,
            stateEvent = stateEvent,
            api = GET_POPULAR_MOVIES to {
                apiService.getPopularMovies(
                    page = 1
                )
            },
            cacheCall = { database.popularMovieDao().findAllMovies() },
            apiErrorUIComponentType = UIComponentType.TwoActionDialog(
                positiveButtonText = R.string.text_cancel,
                negativeButtonText = R.string.text_load_cache,
                twoActionDialogCallback = apiErrorCallback
            )
        ) {
            override fun shouldCallNetwork(): Boolean = if (isFromCache) false else isConnected

            override suspend fun handleNetworkSuccess(resultObj: MovieResponse): DataState<MainViewState> =
                returnPopularMovies(
                    stateEvent = stateEvent,
                    resultObj = mergePopularMovie(
                        serverData = resultObj.movies,
                        popularMovieDao = database.popularMovieDao()
                    )
                )

            override suspend fun handleCacheSuccess(resultObj: List<PopularMovie>): DataState<MainViewState> =
                returnPopularMovies(
                    stateEvent = stateEvent,
                    resultObj = resultObj
                )

        }.result

    override suspend fun getUpcomingMovies(
        stateEvent: StateEvent,
        isConnected: Boolean,
        isFromCache: Boolean,
        apiErrorCallback: TwoActionDialogCallback
    ): Flow<DataState<MainViewState>> =
        object : NetworkBoundResource<MovieResponse, List<UpcomingMovie>, MainViewState>(
            dispatcher = ioDispatcher,
            stateEvent = stateEvent,
            api = GET_UPCOMING_MOVIES to {
                apiService.getUpcomingMovies(
                    page = 1
                )
            },
            cacheCall = { database.upcomingMovieDao().findAllMovies() },
            apiErrorUIComponentType = UIComponentType.TwoActionDialog(
                positiveButtonText = R.string.text_cancel,
                negativeButtonText = R.string.text_load_cache,
                twoActionDialogCallback = apiErrorCallback
            )
        ) {
            override fun shouldCallNetwork(): Boolean = if (isFromCache) false else isConnected

            override suspend fun handleNetworkSuccess(resultObj: MovieResponse): DataState<MainViewState> =
                returnUpcomingMovies(
                    stateEvent = stateEvent,
                    resultObj = mergeUpcomingMovie(
                        serverData = resultObj.movies,
                        upcomingMovieDao = database.upcomingMovieDao()
                    )
                )

            override suspend fun handleCacheSuccess(resultObj: List<UpcomingMovie>): DataState<MainViewState> =
                returnUpcomingMovies(
                    stateEvent = stateEvent,
                    resultObj = resultObj
                )

        }.result

    suspend fun mergePopularMovie(
        serverData: List<Movie001>,
        popularMovieDao: PopularMovieDao
    ): List<PopularMovie> {
        val serverDataMap =
            serverData.map { PopularMovie(it) }.associateBy { it.id }.toMutableMap()

        popularMovieDao.insertMovies(
            serverDataMap.values.toList()
        )
        return popularMovieDao.findAllMovies()
    }

    private fun returnPopularMovies(
        stateEvent: StateEvent,
        resultObj: List<PopularMovie>
    ) = DataState.data(
        data = MainViewState(
            moviesField = MoviesField(
                popularDataSource = MutableLiveData(
                    resultObj
                )
            )
        ),
        response = null,
        stateEvent = stateEvent
    )

    suspend fun mergeUpcomingMovie(
        serverData: List<Movie001>,
        upcomingMovieDao: UpcomingMovieDao
    ): List<UpcomingMovie> {
        val serverDataMap =
            serverData.map { UpcomingMovie(it) }.associateBy { it.id }.toMutableMap()

        upcomingMovieDao.insertMovies(
            serverDataMap.values.toList()
        )
        return upcomingMovieDao.findAllMovies()
    }

    private fun returnUpcomingMovies(
        stateEvent: StateEvent,
        resultObj: List<UpcomingMovie>
    ) = DataState.data(
        data = MainViewState(
            moviesField = MoviesField(
                upcomingDataSource = MutableLiveData(
                    resultObj
                )
            )
        ),
        response = null,
        stateEvent = stateEvent
    )
}