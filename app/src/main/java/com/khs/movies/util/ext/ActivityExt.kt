package com.khs.movies.util.ext

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import com.khs.movies.model.common.StateMessageCallback

fun Activity.displayToast(
    formatArgs: Array<Any>? = null,
    @StringRes message: Int,
    length: Int = Toast.LENGTH_LONG,
    stateMessageCallback: StateMessageCallback? = null
) {
    Toast.makeText(
        this,
        formatArgs?.let {
            resources.getString(message, *it)
        } ?: resources.getString(message),
        length
    ).show()
    stateMessageCallback?.removeMessageFromStack()
}