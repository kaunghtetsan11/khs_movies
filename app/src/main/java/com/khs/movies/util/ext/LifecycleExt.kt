package com.khs.movies.util.ext

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.khs.movies.util.framework.DialogLifecycleObserver
import com.khs.movies.util.framework.SnackBarLifecycleObserver

fun AlertDialog.lifecycleOwner(owner: LifecycleOwner? = null): AlertDialog {
    val observer =
        DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
        ?: throw IllegalStateException(
            "$context is not a LifecycleOwner."
        ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}

fun DialogFragment.lifecycleOwner(owner: LifecycleOwner? = null): DialogFragment {
    val observer =
        DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
        ?: throw IllegalStateException(
            "$context is not a LifecycleOwner."
        ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}

fun Snackbar.lifecycleOwner(owner: LifecycleOwner? = null): Snackbar {
    val observer =
        SnackBarLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
        ?: throw IllegalStateException(
            "$context is not a LifecycleOwner."
        ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}