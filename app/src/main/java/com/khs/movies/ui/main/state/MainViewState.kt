package com.khs.movies.ui.main.state

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.khs.movies.model.common.Event
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.persistance.entitiy.UpcomingMovie
import com.khs.movies.util.framework.AppConnectivityManager


data class MainViewState(
    var moviesField: MoviesField? = null,
    var movieDetailField: MovieDetailField? = null,
)

class MoviesField(
    var popularDataSource: MutableLiveData<List<PopularMovie>> = MutableLiveData(),
    var upcomingDataSource: MutableLiveData<List<UpcomingMovie>> = MutableLiveData(),
) {
    var isCheckFavorite = MutableLiveData<Event<Boolean>>()

    val isProgressShow = MutableLiveData(false)
    val isProgressShowUpcoming = MutableLiveData(false)

    val isConnected = MutableLiveData(true)
}

class MovieDetailField {
    var isCheckFavorite = MutableLiveData<Event<Boolean>>()
    var popularMovie: MutableLiveData<PopularMovie> = MutableLiveData()
    var upcomingMovie: MutableLiveData<UpcomingMovie> = MutableLiveData()
}
