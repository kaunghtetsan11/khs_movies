package com.khs.movies.ui


import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.khs.movies.R
import com.khs.movies.model.common.*
import com.khs.movies.util.ext.displaySnackBar
import com.khs.movies.util.ext.displayToast
import com.khs.movies.util.ext.lifecycleOwner

abstract class BaseActivity : AppCompatActivity(),
    UICommunicationListener {

    override fun onResponseReceived(
        lifecycleOwner: LifecycleOwner,
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {

        when (response.uiComponentType) {

            is UIComponentType.SnackBar -> with(response.uiComponentType) {
                getRootView().displaySnackBar(
                    lifecycleOwner = this@BaseActivity,
                    formatArgs = response.formatArgs,
                    message = response.message,
                    actionText = actionText,
                    onClickAction = onClickAction,
                    length = length ?: Snackbar.LENGTH_LONG,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.Toast -> with(response) {
                displayToast(
                    formatArgs = response.formatArgs,
                    message = message,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.Dialog -> with(response.uiComponentType) {
                displayDialog(
                    lifecycleOwner = lifecycleOwner,
                    response = response,
                    positiveButtonText = positiveButtonText,
                    oneActionDialogCallback = oneActionDialogCallback,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.TwoActionDialog -> with(response.uiComponentType) {
                displayDualActionDialog(
                    lifecycleOwner = lifecycleOwner,
                    response = response,
                    positiveButtonText = positiveButtonText,
                    negativeButtonText = negativeButtonText,
                    twoActionDialogCallback = twoActionDialogCallback,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.TextView -> Unit

            is UIComponentType.None ->
                stateMessageCallback.removeMessageFromStack()
        }
    }

    protected abstract fun getRootView(): View

    private fun displayDialog(
        lifecycleOwner: LifecycleOwner,
        response: Response,
        @StringRes positiveButtonText: Int,
        oneActionDialogCallback: OneActionDialogCallback?,
        stateMessageCallback: StateMessageCallback
    ): AlertDialog =
        with(response) {
            displayOneActionDialog(
                lifecycleOwner = lifecycleOwner,
                title = when (response.messageType) {
                    is MessageType.Error -> R.string.text_error
                    is MessageType.Success -> R.string.text_success
                    is MessageType.Info -> R.string.text_info
                    is MessageType.Warning -> R.string.text_warning
                },
                formatArgs = formatArgs,
                message = message,
                messageStr = null,
                buttonText = positiveButtonText,
                isNeedZawgyiTypeFace = isNeedZawgyiTypeFace,
                oneActionDialogCallback = oneActionDialogCallback,
                stateMessageCallback = stateMessageCallback
            )
        }

    private fun displayDualActionDialog(
        lifecycleOwner: LifecycleOwner,
        response: Response,
        @StringRes positiveButtonText: Int,
        @StringRes negativeButtonText: Int,
        twoActionDialogCallback: TwoActionDialogCallback,
        stateMessageCallback: StateMessageCallback
    ): AlertDialog =
        with(response) {
            displayTwoActionDialog(
                lifecycleOwner = lifecycleOwner,
                title = when (response.messageType) {
                    is MessageType.Error -> R.string.text_error
                    is MessageType.Success -> R.string.text_success
                    is MessageType.Info -> R.string.text_info
                    is MessageType.Warning -> R.string.text_warning
                },
                formatArgs = formatArgs,
                message = message,
                messageStr = null,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                isNeedZawgyiTypeFace = isNeedZawgyiTypeFace,
                twoActionDialogCallback = twoActionDialogCallback,
                stateMessageCallback = stateMessageCallback
            )
        }

    override fun displayOneActionDialog(
        lifecycleOwner: LifecycleOwner,
        @StringRes title: Int,
        formatArgs: Array<Any>?,
        @StringRes message: Int,
        messageStr: String?,
        @StringRes buttonText: Int,
        isNeedZawgyiTypeFace: Boolean?,
        oneActionDialogCallback: OneActionDialogCallback?,
        stateMessageCallback: StateMessageCallback?
    ): AlertDialog {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(title)
            messageStr?.let {
                setMessage(it)
            } ?: setMessage(
                formatArgs?.let {
                    getString(message, *it)
                } ?: getString(message)
            )
            setPositiveButton(buttonText) { _, _ ->
                stateMessageCallback?.removeMessageFromStack()
                oneActionDialogCallback?.positive()
            }
            setCancelable(false)
            return create().apply {
                lifecycleOwner(lifecycleOwner)
                show()
            }
        }
    }

    override fun displayTwoActionDialog(
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
    ): AlertDialog {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(title)
            messageStr?.let {
                setMessage(it)
            } ?: setMessage(
                formatArgs?.let {
                    getString(message, *it)
                } ?: getString(message)
            )
            setPositiveButton(positiveButtonText) { _, _ ->
                stateMessageCallback?.removeMessageFromStack()
                twoActionDialogCallback.positive()
            }
            setNegativeButton(negativeButtonText) { _, _ ->
                stateMessageCallback?.removeMessageFromStack()
                twoActionDialogCallback.negative()
            }
            setCancelable(false)
            return create().apply {
                lifecycleOwner(lifecycleOwner)
                show()
            }
        }
    }

}









