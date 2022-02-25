package com.khs.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.movies.model.common.DataState
import com.khs.movies.model.common.StateEvent
import com.khs.movies.model.common.StateMessage
import com.khs.movies.util.manager.DataChannelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<ViewState> : ViewModel() {
    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()

    private val dataChannelManager: DataChannelManager<ViewState> =
        object : DataChannelManager<ViewState>() {

            override fun handleNewData(data: ViewState) {
                this@BaseViewModel.handleNewData(data)
            }
        }

    val viewState: LiveData<ViewState>
        get() = _viewState

    val numActiveJobs: LiveData<Int> = dataChannelManager.numActiveJobs

    val activeJobType: LiveData<Int?> = dataChannelManager.activeJobType

    val stateMessage: LiveData<StateMessage?>
        get() = dataChannelManager.messageStack.stateMessage

    protected abstract fun handleNewData(data: ViewState)

    abstract fun setStateEvent(
        stateEvent: StateEvent,
        coroutineScope: CoroutineScope
    )

    suspend fun launchJob(
        flowDataState: Flow<DataState<ViewState>>
    ) {
        dataChannelManager.launchJob(
            flowDataState
        )
    }

    fun postDataState(dataState: DataState<ViewState>) {
        dataChannelManager.postDataState(dataState)
    }

    private fun areAnyJobsActive() = dataChannelManager.numActiveJobs.value?.let {
        it > 0
    } ?: false

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return dataChannelManager.isJobAlreadyActive(stateEvent)
    }

    fun addStateEvent(stateEvent: StateEvent) {
        dataChannelManager.addStateEvent(stateEvent)
    }

    fun getCurrentViewStateOrNew() = viewState.value ?: initNewViewState()

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState!!
    }

    fun clearStateMessage(index: Int = 0) {
        dataChannelManager.clearStateMessage(index)
    }

    fun cancelActiveJobs() {
        if (areAnyJobsActive()) {
            dataChannelManager.cancelJobs()
        }
    }

    protected abstract fun initNewViewState(): ViewState

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}