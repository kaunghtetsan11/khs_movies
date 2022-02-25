package com.khs.movies.persistance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khs.movies.persistance.entitiy.PopularMovie

@Dao
interface PopularMovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(list: List<PopularMovie>)

    @Query("SELECT * FROM POPULAR_MOVIE")
    suspend fun findAllMovies(): List<PopularMovie>

    @Query("UPDATE POPULAR_MOVIE SET IS_FAVORITE = :isFavorite WHERE ID = :id")
    suspend fun updateIsFavoriteById(id: Long, isFavorite: Boolean)

    @Query("SELECT * FROM POPULAR_MOVIE WHERE ID = :id ")
    suspend fun findFavoriteMovie(id: Long): PopularMovie

}