package com.khs.movies.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetail(
    var id: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    val isPopular: Boolean = false
) : Parcelable
