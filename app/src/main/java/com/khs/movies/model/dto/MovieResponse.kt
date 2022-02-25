package com.khs.movies.model.dto

import com.khs.movies.model.common.IMAGE_URL
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(

    val dates: Date? = null,

    val page: Int,

    @Json(name = "results")
    val movies: List<Movie001>,

    @Json(name = "total_pages")
    val totalPages: Int,

    @Json(name = "total_results")
    val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class Date(
    val maximum: String,
    val minimum: String
)

@JsonClass(generateAdapter = true)
data class Movie001(
    val adult: Boolean,

    @Json(name = "backdrop_path")
    val backdropPath: String? = null,

    @Json(name = "genre_ids")
    val genreIds: List<Int>,

    val id: Long,

    @Json(name = "original_language")
    val originalLanguage: String,

    @Json(name = "original_title")
    val originalTitle: String,

    val overview: String,

    val popularity: Double,

    @Json(name = "poster_path")
    val posterPath: String,

    @Json(name = "release_date")
    val releaseDate: String,

    val title: String,

    val video: Boolean,

    @Json(name = "vote_average")
    val voteAverage: Float,

    @Json(name = "vote_count")
    val voteCount: Int

) {
    val imageUrl = IMAGE_URL + posterPath

    val rating = voteAverage.div(2)
}