package com.khs.movies.model.common


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.parcelize.IgnoredOnParcel


class MessageStack : ArrayList<StateMessage>() {

    @IgnoredOnParcel
    private val _stateMessage: MutableLiveData<StateMessage?> = MutableLiveData()

    @IgnoredOnParcel
    val stateMessage: LiveData<StateMessage?>
        get() = _stateMessage

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for (element in elements) {
            add(element)
        }
        return true // always return true. We don't care about result bool.
    }

    override fun add(element: StateMessage): Boolean {
        if (this.contains(element)) { // prevent duplicate errors added to stack
            return false
        }
        val transaction = super.add(element)
        if (this.size == 1) {
            setStateMessage(stateMessage = element)
        }
        return transaction
    }

    override fun removeAt(index: Int): StateMessage =
        try {
            val transaction = super.removeAt(index)
            if (this.size > 0) {
                setStateMessage(stateMessage = this[0])
            } else {
                setStateMessage(null)
            }
            transaction
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            throw e
        }

    private fun setStateMessage(stateMessage: StateMessage?) {
        _stateMessage.value = stateMessage
    }
}