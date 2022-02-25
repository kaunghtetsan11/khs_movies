package com.khs.movies.ui.main.viewmodel

import com.khs.movies.model.common.Event
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.persistance.entitiy.UpcomingMovie
import com.khs.movies.ui.main.state.MovieDetailField
import com.khs.movies.ui.main.state.MoviesField

/************************************************Movie*******************************************************/
fun MainViewModel.initMoviesField() {
    setViewState(getCurrentViewStateOrNew().apply {
        moviesField = moviesField ?: MoviesField()
    })
}

fun MainViewModel.setMoviesFieldConnected(flag: Boolean) {
    with(getCurrentViewStateOrNew()) {
        moviesField?.isConnected?.value = flag
    }
}

fun MainViewModel.setMoviesFieldPopularMovieDataSource(dataSource: List<PopularMovie>) {
    with(getCurrentViewStateOrNew()) {
        moviesField?.popularDataSource?.value = dataSource
    }
}

fun MainViewModel.setMoviesFieldUpcomingMovieDataSource(dataSource: List<UpcomingMovie>) {
    with(getCurrentViewStateOrNew()) {
        moviesField?.upcomingDataSource?.value = dataSource
    }
}

fun MainViewModel.setMoviesFieldProgressShow(flag: Boolean) {
    with(getCurrentViewStateOrNew()) {
        moviesField?.isProgressShow?.value = flag
    }
}

fun MainViewModel.setMoviesFieldProgressShowUpcoming(flag: Boolean) {
    with(getCurrentViewStateOrNew()) {
        moviesField?.isProgressShowUpcoming?.value = flag
    }
}

fun MainViewModel.setMoviesFieldIsCheckedFavorite(flag: Boolean) {
    with(getCurrentViewStateOrNew()) {
        moviesField?.isCheckFavorite?.value = Event(flag)
    }
}



fun MainViewModel.clearMoviesField() {
    setViewState(getCurrentViewStateOrNew().apply {
        moviesField = null
    })
}

/************************************************Movie Detail*******************************************************/


fun MainViewModel.initMovieDetailField() {
    setViewState(getCurrentViewStateOrNew().apply {
        movieDetailField = movieDetailField ?: MovieDetailField()
    })
}



fun MainViewModel.setMovieDetailFieldPopularMovie(movie: PopularMovie) {
    with(getCurrentViewStateOrNew()) {
        movieDetailField?.popularMovie?.value = movie
    }
}



fun MainViewModel.setMovieDetailFieldUpcomingMovie(movie: UpcomingMovie) {
    with(getCurrentViewStateOrNew()) {
        movieDetailField?.upcomingMovie?.value = movie
    }
}



fun MainViewModel.setMovieDetailFieldIsCheckedFavorite(flag: Boolean) {
    with(getCurrentViewStateOrNew()) {
        movieDetailField?.isCheckFavorite?.value = Event(flag)
    }
}



fun MainViewModel.clearMovieDetailField() {
    setViewState(getCurrentViewStateOrNew().apply {
        movieDetailField = null
    })
}
