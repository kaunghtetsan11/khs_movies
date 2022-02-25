package com.khs.movies.ui

import androidx.lifecycle.ViewModel

class FragmentViewModel : ViewModel() {

    var clear: (() -> Unit)? = null

    inline fun initData(block: (() -> Unit)) {
        block()
    }

    override fun onCleared() {
        super.onCleared()
        clear?.invoke()
    }
}