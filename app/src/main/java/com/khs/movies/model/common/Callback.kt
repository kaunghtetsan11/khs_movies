package com.khs.movies.model.common

interface OneActionDialogCallback {
    fun positive()
}

interface TwoActionDialogCallback :
    OneActionDialogCallback {
    fun negative()
}

interface ThreeActionDialogCallback :
    TwoActionDialogCallback {
    fun neutral()
}