package com.khs.movies.ui.main.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview



fun MainViewModel.getMoviesFields() = viewState.value?.moviesField!!



fun MainViewModel.getMoviesDetailFields() = viewState.value?.movieDetailField!!