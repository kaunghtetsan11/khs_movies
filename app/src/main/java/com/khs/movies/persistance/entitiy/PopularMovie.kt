package com.khs.movies.persistance.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khs.movies.model.dto.Movie001

@Entity(tableName = "POPULAR_MOVIE")
data class PopularMovie(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    var id: Long,

    @ColumnInfo(name = "TITLE")
    val title: String,

    @ColumnInfo(name = "OVERVIEW")
    val overview: String,

    @ColumnInfo(name = "POSTER")
    val posterPath: String,

    @ColumnInfo(name = "IS_FAVORITE")
    val isFavorite: Boolean = false
) {
    constructor(movie: Movie001) : this(
        id = movie.id,
        title = movie.title,
        overview = movie.overview,
        posterPath = movie.imageUrl
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PopularMovie

        if (id != other.id) return false
        if (title != other.title) return false
        if (overview != other.overview) return false
        if (posterPath != other.posterPath) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + posterPath.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }


}
