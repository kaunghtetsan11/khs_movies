package com.khs.movies.binding

import android.view.View

fun setViewRender(isShow: Boolean?): Int {
    return isShow?.let {
        if (it) View.VISIBLE else View.GONE
    } ?: View.GONE
}