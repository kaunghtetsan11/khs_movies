package com.khs.movies.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.movies.model.common.Event
import com.khs.movies.model.common.StateEvent
import com.khs.movies.repository.CacheRepository
import com.khs.movies.repository.MainRepository
import com.khs.movies.ui.BaseViewModel
import com.khs.movies.ui.main.state.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository,
    val cacheRepository: CacheRepository,
) : BaseViewModel<MainViewState>() {

    override fun handleNewData(data: MainViewState) {
        with(data) {
            when {
                moviesField != null -> handleMovieFields(moviesField!!)
                movieDetailField != null -> handleMovieDetailFields(movieDetailField!!)
            }
        }
    }

    override fun setStateEvent(
        stateEvent: StateEvent,
        coroutineScope: CoroutineScope
    ) {
        with(stateEvent) {
            if (!isJobAlreadyActive(this)) {
                addStateEvent(this)
                coroutineScope.launch {
                    if (isSingleShot)
                        postDataState(getDataState(this@with))
                    else launchJob(
                        flowDataState = getFlowDataState(this@with)
                    )
                }
            }
        }
    }

    override fun initNewViewState() = MainViewState()

}