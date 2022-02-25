package com.khs.movies.model.common


import android.view.View
import androidx.annotation.StringRes
import com.khs.movies.R


data class StateMessage(val response: Response)

data class Response(
    val formatArgs: Array<Any>? = null,
    @StringRes val message: Int,
    val isNeedZawgyiTypeFace: Boolean? = null,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (formatArgs != null) {
            if (other.formatArgs == null) return false
            if (!formatArgs.contentEquals(other.formatArgs)) return false
        } else if (other.formatArgs != null) return false
        if (message != other.message) return false
        if (isNeedZawgyiTypeFace != other.isNeedZawgyiTypeFace) return false
        if (uiComponentType != other.uiComponentType) return false
        if (messageType != other.messageType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = formatArgs?.contentHashCode() ?: 0
        result = 31 * result + message
        result = 31 * result + (isNeedZawgyiTypeFace?.hashCode() ?: 0)
        result = 31 * result + uiComponentType.hashCode()
        result = 31 * result + messageType.hashCode()
        return result
    }
}

sealed class UIComponentType {

    object Toast : UIComponentType()

    class SnackBar(
        @StringRes val actionText: Int? = null,
        val onClickAction: (View.() -> Unit)? = null,
        val length: Int? = null
    ) : UIComponentType()

    class Dialog(
        @StringRes val positiveButtonText: Int = R.string.text_ok,
        val oneActionDialogCallback: OneActionDialogCallback? = null
    ) : UIComponentType()

    class TwoActionDialog(
        @StringRes val positiveButtonText: Int = R.string.text_ok,
        @StringRes val negativeButtonText: Int,
        val twoActionDialogCallback: TwoActionDialogCallback
    ) : UIComponentType()

    object TextView : UIComponentType()

    object None : UIComponentType()
}

sealed class MessageType {

    object Success : MessageType()

    object Error : MessageType()

    object Info : MessageType()

    object Warning : MessageType()
}

interface StateMessageCallback {

    fun removeMessageFromStack()
}

interface StateEvent {
    @StateEventType
    val type: Int
    val isSingleShot: Boolean
}
