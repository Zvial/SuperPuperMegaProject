package com.example.superpupermegaproject.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

internal val moviesTableContract = MoviesDatabase.Companion.DatabaseContract.moviesTable

@Entity(tableName = moviesTableContract.NAME)
data class MovieEntity(
        /*@PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_ID)
        val id: Long? = null,*/
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_ID)
        val movieID: Long,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_POSITION)
        val position: Long,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_TITLE)
        val title: String,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_OVERVIEW)
        val overview: String,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_POSTER)
        val poster: String?,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_BACKDROP)
        val backdrop: String?,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_RATINGS)
        var ratings: Float,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_ADULT)
        val adult: Boolean,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_RUNTIME)
        val runtime: Int,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_VOTECOUNT)
        val voteCount: Int,

        @ColumnInfo(name = moviesTableContract.COLUMN_NAME_ISLIKED)
        var isLiked: Boolean
)