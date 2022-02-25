package com.khs.movies.util.ext

import android.os.SystemClock
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.khs.movies.model.common.StateMessageCallback
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


fun View.displaySnackBar(
    lifecycleOwner: LifecycleOwner,
    formatArgs: Array<Any>? = null,
    @StringRes message: Int,
    @StringRes actionText: Int? = null,
    onClickAction: (View.() -> Unit)? = null,
    length: Int,
    stateMessageCallback: StateMessageCallback? = null
): Snackbar =
    Snackbar.make(
        this,
        formatArgs?.let {
            context.resources.getString(message, *it)
        } ?: context.resources.getString(message),
        length
    ).apply {
        if (actionText != null && onClickAction != null) {
            setAction(
                actionText,
                onClickAction
            )
        }
        lifecycleOwner(lifecycleOwner)
        stateMessageCallback?.removeMessageFromStack()
        show()
    }

inline fun View.setOnClickDebounceListener(
    debounceTime: Long = 600L,
    crossinline action: View.() -> Unit
) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else v.action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun RecyclerView.setUpOverScrollAndFixedSize(isFixedSize: Boolean = true) {
    setHasFixedSize(isFixedSize)
    OverScrollDecoratorHelper.setUpOverScroll(
        this,
        OverScrollDecoratorHelper.ORIENTATION_VERTICAL
    )
}
