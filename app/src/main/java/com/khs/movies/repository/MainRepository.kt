package com.khs.movies.repository

import com.khs.movies.model.common.DataState
import com.khs.movies.model.common.StateEvent
import com.khs.movies.model.common.TwoActionDialogCallback
import com.khs.movies.ui.main.state.MainViewState
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getPopularMovies(
        stateEvent: StateEvent,
        isConnected: Boolean,
        isFromCache: Boolean,
        apiErrorCallback: TwoActionDialogCallback
    ): Flow<DataState<MainViewState>>

    suspend fun getUpcomingMovies(
        stateEvent: StateEvent,
        isConnected: Boolean,
        isFromCache: Boolean,
        apiErrorCallback: TwoActionDialogCallback
    ): Flow<DataState<MainViewState>>

}