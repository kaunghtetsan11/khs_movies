package com.khs.movies.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khs.movies.persistance.dao.PopularMovieDao
import com.khs.movies.persistance.dao.UpcomingMovieDao
import com.khs.movies.persistance.entitiy.PopularMovie
import com.khs.movies.persistance.entitiy.UpcomingMovie

@Database(
    entities = [
        PopularMovie::class, UpcomingMovie::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun popularMovieDao(): PopularMovieDao

    abstract fun upcomingMovieDao(): UpcomingMovieDao
}