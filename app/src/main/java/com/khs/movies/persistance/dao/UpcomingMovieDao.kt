package com.khs.movies.persistance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khs.movies.persistance.entitiy.UpcomingMovie

@Dao
interface UpcomingMovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(list: List<UpcomingMovie>)

    @Query("SELECT * FROM UPCOMING_MOVIE")
    suspend fun findAllMovies(): List<UpcomingMovie>

    @Query("UPDATE UPCOMING_MOVIE SET IS_FAVORITE = :isFavorite WHERE ID = :id")
    suspend fun updateIsFavoriteById(id: Long, isFavorite: Boolean)

    @Query("SELECT * FROM UPCOMING_MOVIE WHERE ID = :id")
    suspend fun findFavoriteMovie(id: Long): UpcomingMovie

}