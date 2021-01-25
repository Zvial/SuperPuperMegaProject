package com.example.superpupermegaproject.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.superpupermegaproject.data.Genre

internal val moviesGenresTableContract = MoviesDatabase.Companion.DatabaseContract.moviesGenresTable

@Entity(tableName = moviesGenresTableContract.NAME, primaryKeys = arrayOf(moviesGenresTableContract.COLUMN_NAME_GENREID, moviesGenresTableContract.COLUMN_NAME_MOVIEID))
data class MoviesGenresEntity(
    @ColumnInfo(name = moviesGenresTableContract.COLUMN_NAME_GENREID)
    @ForeignKey(
        entity = GenresEntity::class,
        childColumns = arrayOf(moviesGenresTableContract.COLUMN_NAME_GENREID),
        parentColumns = arrayOf(MoviesDatabase.Companion.DatabaseContract.genresTable.COLUMN_NAME_ID)
    )
    val genreID: Long,
    @ColumnInfo(name = moviesGenresTableContract.COLUMN_NAME_MOVIEID)
    @ForeignKey(
        entity = MovieEntity::class,
        childColumns = arrayOf(moviesGenresTableContract.COLUMN_NAME_MOVIEID),
        parentColumns = arrayOf(MoviesDatabase.Companion.DatabaseContract.moviesTable.COLUMN_NAME_ID)
    )
    val movieID: Long
)