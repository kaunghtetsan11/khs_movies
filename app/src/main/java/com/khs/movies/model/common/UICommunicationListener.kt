package com.khs.movies.model.common

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner

interface UICommunicationListener {

    fun onResponseReceived(
        lifecycleOwner: LifecycleOwner,
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayOneActionDialog(
        lifecycleOwner: LifecycleOwner,
        @StringRes title: Int,
        formatArgs: Array<Any>? = null,
        @StringRes message: Int,
        messageStr: String? = null,
        @StringRes buttonText: Int,
        isNeedZawgyiTypeFace: Boolean? = null,
        oneActionDialogCallback: OneActionDialogCallback? = null,
        stateMessageCallback: StateMessageCallback? = null
    ): AlertDialog

    fun displayTwoActionDialog(
        lifecycleOwner: LifecycleOwner,
        @StringRes title: Int,
        formatArgs: Array<Any>?,
        @StringRes message: Int,
        messageStr: String?,
        @StringRes positiveButtonText: Int,
        @StringRes negativeButtonText: Int,
        isNeedZawgyiTypeFace: Boolean?,
        twoActionDialogCallback: TwoActionDialogCallback,
        stateMessageCallback: StateMessageCallback?
    ): AlertDialog
}