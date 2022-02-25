package com.khs.movies.ui.main.state

import com.khs.movies.model.common.*


sealed class MainStateEvent : StateEvent {

    data class GetPopularMoviesEvent(
        val isConnected: Boolean,
        val isFromCache: Boolean,
        val apiErrorCallback: TwoActionDialogCallback
    ) : MainStateEvent() {
        override val type: Int
            get() = GET_POPULAR_MOVIES_EVENT
        override val isSingleShot: Boolean
            get() = false
    }

    data class GetUpcomingMoviesEvent(
        val isConnected: Boolean,
        val isFromCache: Boolean,
        val apiErrorCallback: TwoActionDialogCallback
    ) : MainStateEvent() {
        override val type: Int
            get() = GET_UPCOMING_MOVIES_EVENT
        override val isSingleShot: Boolean
            get() = false
    }

    data class UpdateIsFavoriteEvent(
        val id: Long,
        val isPopular: Boolean,
        val isFavorite: Boolean,
        val successCallBack: OneActionDialogCallback
    ) : MainStateEvent() {
        override val type: Int
            get() = UPDATE_IS_FAVORITE_EVENT
        override val isSingleShot: Boolean
            get() = false
    }

    data class GetFavoriteMovieEvent(
        val id: Long,
        val isPopular: Boolean
    ) : MainStateEvent() {
        override val type: Int
            get() = GET_IS_FAVORITE_EVENT
        override val isSingleShot: Boolean
            get() = false
    }

}