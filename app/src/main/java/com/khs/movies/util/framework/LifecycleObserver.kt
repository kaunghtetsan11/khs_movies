package com.khs.movies.util.framework

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class DialogLifecycleObserver(private val dismiss: () -> Unit) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = dismiss()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() = dismiss()
}

class SnackBarLifecycleObserver(private val dismiss: () -> Unit) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = dismiss()

//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    fun onPause() = dismiss()
}