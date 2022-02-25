package com.khs.movies.api

import com.khs.movies.model.dto.MovieResponse
import com.khs.movies.model.common.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): MovieResponse

    @GET("upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): MovieResponse
}