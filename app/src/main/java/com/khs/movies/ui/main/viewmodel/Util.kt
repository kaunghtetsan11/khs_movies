package com.khs.movies.ui.main.viewmodel

import com.khs.movies.R
import com.khs.movies.model.common.*
import com.khs.movies.ui.main.state.MainStateEvent
import com.khs.movies.ui.main.state.MainStateEvent.GetPopularMoviesEvent
import com.khs.movies.ui.main.state.MainStateEvent.GetUpcomingMoviesEvent
import com.khs.movies.ui.main.state.MainViewState
import com.khs.movies.ui.main.state.MovieDetailField
import com.khs.movies.ui.main.state.MoviesField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


fun MainViewModel.handleMovieFields(moviesField: MoviesField) {
    with(moviesField) {
        popularDataSource.value?.let {
            setMoviesFieldPopularMovieDataSource(it)
            return
        }

        upcomingDataSource.value?.let {
            setMoviesFieldUpcomingMovieDataSource(it)
        }
    }
}

fun MainViewModel.handleMovieDetailFields(movieDetailField: MovieDetailField) {
    with(movieDetailField) {
        upcomingMovie.value?.let {
            setMovieDetailFieldUpcomingMovie(it)
            return
        }

        popularMovie.value?.let {
            setMovieDetailFieldPopularMovie(it)
        }
    }
}



suspend fun MainViewModel.getDataState(
    stateEvent: StateEvent
): DataState<MainViewState> =

    with(cacheRepository) {
        when (stateEvent) {


            else ->
                DataState.error(
                    response = Response(
                        message = R.string.error_invalid_event,
                        uiComponentType = UIComponentType.None,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
        }
    }


suspend fun MainViewModel.getFlowDataState(
    stateEvent: StateEvent
): Flow<DataState<MainViewState>> = with(mainRepository) {
    with(cacheRepository) {
        when (stateEvent) {
            is GetPopularMoviesEvent -> with(stateEvent) {
                getPopularMovies(
                    stateEvent = this,
                    isConnected = isConnected,
                    isFromCache = isFromCache,
                    apiErrorCallback = apiErrorCallback
                )
            }

            is GetUpcomingMoviesEvent -> with(stateEvent) {
                getUpcomingMovies(
                    stateEvent = this,
                    isConnected = isConnected,
                    isFromCache = isFromCache,
                    apiErrorCallback = apiErrorCallback
                )
            }

            is MainStateEvent.UpdateIsFavoriteEvent -> with(stateEvent) {
                updateIsFavoriteById(
                    stateEvent = this,
                    id = id,
                    isPopular = isPopular,
                    isFavorite = isFavorite,
                    successCallBack = successCallBack
                )
            }

            is MainStateEvent.GetFavoriteMovieEvent -> with(stateEvent) {
                getFavoriteMovie(
                    stateEvent = this,
                    id = id,
                    isPopular = isPopular
                )
            }

            else -> flow {
                emit(
                    DataState.error(
                        response = Response(
                            message = R.string.error_invalid_event,
                            uiComponentType = UIComponentType.None,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                )
            }
        }
    }

}