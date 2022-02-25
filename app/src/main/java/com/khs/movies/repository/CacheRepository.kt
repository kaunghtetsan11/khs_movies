package com.khs.movies.repository

import com.khs.movies.model.common.DataState
import com.khs.movies.model.common.OneActionDialogCallback
import com.khs.movies.model.common.StateEvent
import com.khs.movies.ui.main.state.MainViewState
import kotlinx.coroutines.flow.Flow

interface CacheRepository {
    suspend fun updateIsFavoriteById(
        stateEvent: StateEvent,
        id: Long,
        isPopular: Boolean,
        isFavorite: Boolean,
        successCallBack: OneActionDialogCallback
    ): Flow<DataState<MainViewState>>

    suspend fun getFavoriteMovie(
        stateEvent: StateEvent,
        id: Long,
        isPopular: Boolean
    ): Flow<DataState<MainViewState>>
}