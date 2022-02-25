package com.khs.movies.repository

import androidx.lifecycle.MutableLiveData
import com.khs.movies.R
import com.khs.movies.model.common.*
import com.khs.movies.persistance.AppDatabase
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.persistance.entitiy.UpcomingMovie
import com.khs.movies.ui.main.state.MainViewState
import com.khs.movies.ui.main.state.MovieDetailField
import com.khs.movies.util.ext.safeCacheCall
import com.khs.movies.util.manager.CacheResponseHandler
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class CacheRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : CacheRepository {
    override suspend fun updateIsFavoriteById(
        stateEvent: StateEvent,
        id: Long,
        isPopular: Boolean,
        isFavorite: Boolean,
        successCallBack: OneActionDialogCallback
    ): Flow<DataState<MainViewState>> = flow {
        val cacheResult = safeCacheCall(ioDispatcher) {
            if (isPopular) {
                db.popularMovieDao().updateIsFavoriteById(id = id, isFavorite = isFavorite)
            } else {
                db.upcomingMovieDao().updateIsFavoriteById(id = id, isFavorite = isFavorite)
            }
        }
        emit(
            object : CacheResponseHandler<MainViewState, Unit>(
                response = cacheResult,
                stateEvent = null,
            ) {
                override suspend fun handleSuccess(resultObj: Unit): DataState<MainViewState> =
                    DataState.data(
                        response = Response(
                            message = R.string.add_to_favorite,
                            uiComponentType = UIComponentType.Dialog(
                                oneActionDialogCallback = successCallBack
                            ),
                            messageType = MessageType.Success
                        ),
                        stateEvent = stateEvent
                    )

            }.getResult()
        )
    }

    override suspend fun getFavoriteMovie(
        stateEvent: StateEvent,
        id: Long,
        isPopular: Boolean
    ): Flow<DataState<MainViewState>> = flow {
        val cacheResult = safeCacheCall(ioDispatcher) {
            db.popularMovieDao().findFavoriteMovie(id = id)
        }
        val cacheResultUpcoming = safeCacheCall(ioDispatcher) {
            db.upcomingMovieDao().findFavoriteMovie(id = id)
        }
        emit(
            if (isPopular) {
                object : CacheResponseHandler<MainViewState, PopularMovie>(
                    response = cacheResult,
                    stateEvent = null,
                ) {
                    override suspend fun handleSuccess(resultObj: PopularMovie): DataState<MainViewState> {
                        return DataState.data(
                            data = MainViewState(
                                movieDetailField = MovieDetailField().apply {
                                    popularMovie = MutableLiveData(resultObj)
                                }
                            ),
                            response = null,
                            stateEvent = stateEvent
                        )
                    }


                }.getResult()
            } else {
                object : CacheResponseHandler<MainViewState, UpcomingMovie>(
                    response = cacheResultUpcoming,
                    stateEvent = null,
                ) {
                    override suspend fun handleSuccess(resultObj: UpcomingMovie): DataState<MainViewState> {
                        return DataState.data(
                            data = MainViewState(
                                movieDetailField = MovieDetailField().apply {
                                    upcomingMovie = MutableLiveData(resultObj)
                                }
                            ),
                            response = null,
                            stateEvent = stateEvent
                        )
                    }


                }.getResult()
            }

        )
    }
}