package com.khs.movies.util.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.movies.model.common.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*


abstract class DataChannelManager<ViewState> {
    private val _activeStateEvents: HashSet<Int> = HashSet()
    private val _numActiveJobs: MutableLiveData<Int> = MutableLiveData()
    private val _activeJobType: MutableLiveData<@StateEventType Int?> = MutableLiveData()
    private val dataChannel: MutableStateFlow<DataState<ViewState>?> =
        MutableStateFlow(null)

    val messageStack = MessageStack()

    val numActiveJobs: LiveData<Int>
        get() = _numActiveJobs

    val activeJobType: LiveData<Int?>
        get() = _activeJobType

    init {
        dataChannel
            .onEach { dataState ->
                dataState?.data?.let { data ->
//                    delay(5000)
                    handleNewData(data)

                    removeStateEvent(dataState.stateEvent)
                }
                dataState?.stateMessage?.let { stateMessage ->
//                    delay(8000)
                    handleNewStateMessage(stateMessage)
                    removeStateEvent(dataState.stateEvent)
                }
            }
            .launchIn(MainScope())
    }

    protected abstract fun handleNewData(data: ViewState)

    private fun offerToDataChannel(dataState: DataState<ViewState>) {
        dataChannel.value = dataState
    }

    suspend fun launchJob(
        flowDataState: Flow<DataState<ViewState>>
    ) {
        coroutineScope {
            flowDataState.collect {
                offerToDataChannel(it)
            }
        }
    }

    fun postDataState(dataState: DataState<ViewState>) {
        offerToDataChannel(dataState)
    }

    private fun handleNewStateMessage(stateMessage: StateMessage) {
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0) {
        with(messageStack) {
            if (isNotEmpty())
                removeAt(index)
        }
    }

    private fun clearActiveStateEventCounter() {
        _activeStateEvents.clear()
        _activeJobType.value = null
        syncNumActiveStateEvents()
    }

    fun addStateEvent(stateEvent: StateEvent) {
        _activeStateEvents.add(stateEvent.type)
        _activeJobType.value = stateEvent.type
        syncNumActiveStateEvents()
    }

    private fun removeStateEvent(stateEvent: StateEvent?) {
        stateEvent?.let {
            _activeStateEvents.remove(stateEvent.type)
            _activeJobType.value = null
            syncNumActiveStateEvents()
        }
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return isStateEventActive(stateEvent) && messageStack.size == 0
    }

    private fun isStateEventActive(stateEvent: StateEvent): Boolean {
        return _activeStateEvents.contains(stateEvent.type)
    }

    fun cancelJobs() {
        clearActiveStateEventCounter()
    }

    private fun syncNumActiveStateEvents() {
        _numActiveJobs.value = _activeStateEvents.size
    }
}