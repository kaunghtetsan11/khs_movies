package com.khs.movies.model.common

import androidx.annotation.IntDef

/* Connection */
@IntDef(
    MOBILE_DATA_CONNECTED,
    WIFI_CONNECTED,
    DISCONNECTED
)
@Retention(AnnotationRetention.SOURCE)
annotation class ConnectionType
const val MOBILE_DATA_CONNECTED = 0
const val WIFI_CONNECTED = 1
const val DISCONNECTED = 2

/* State Event Type */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
@IntDef(
    INVALID_EVENT,
    GET_POPULAR_MOVIES_EVENT,
    GET_UPCOMING_MOVIES_EVENT,
    UPDATE_IS_FAVORITE_EVENT,
    GET_IS_FAVORITE_EVENT
)
@Retention(AnnotationRetention.SOURCE)
annotation class StateEventType

const val INVALID_EVENT = -1
const val GET_POPULAR_MOVIES_EVENT = 1
const val GET_UPCOMING_MOVIES_EVENT = 2
const val UPDATE_IS_FAVORITE_EVENT = 3
const val GET_IS_FAVORITE_EVENT = 4


/* API Method Type */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@IntDef(
    GET_POPULAR_MOVIES,
    GET_UPCOMING_MOVIES
)
@Retention(AnnotationRetention.SOURCE)
annotation class ApiMethod

const val GET_POPULAR_MOVIES = 0
const val GET_UPCOMING_MOVIES = 1
