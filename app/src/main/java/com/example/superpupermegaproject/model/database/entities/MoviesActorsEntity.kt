package com.example.superpupermegaproject.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

internal val moviesActorsTableContract = MoviesDatabase.Companion.DatabaseContract.moviesActorsTable

@Entity(tableName = moviesActorsTableContract.NAME, primaryKeys = arrayOf(moviesActorsTableContract.COLUMN_NAME_ACTORID, moviesActorsTableContract.COLUMN_NAME_MOVIEID))
data class MoviesActorsEntity(
    @ColumnInfo(name = moviesActorsTableContract.COLUMN_NAME_ACTORID)
    @ForeignKey(
        entity = ActorsEntity::class,
        childColumns = arrayOf(moviesActorsTableContract.COLUMN_NAME_ACTORID),
        parentColumns = arrayOf(MoviesDatabase.Companion.DatabaseContract.actorsTable.COLUMN_NAME_ID)
    )
    val actorID: Long,
    @ColumnInfo(name = moviesActorsTableContract.COLUMN_NAME_MOVIEID)
    @ForeignKey(
        entity = MovieEntity::class,
        childColumns = arrayOf(moviesActorsTableContract.COLUMN_NAME_MOVIEID),
        parentColumns = arrayOf(MoviesDatabase.Companion.DatabaseContract.moviesTable.COLUMN_NAME_ID)
    )
    val movieID: Long
)