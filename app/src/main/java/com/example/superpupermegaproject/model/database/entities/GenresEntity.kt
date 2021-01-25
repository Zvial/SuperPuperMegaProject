package com.example.superpupermegaproject.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

internal val genresTableContract = MoviesDatabase.Companion.DatabaseContract.genresTable

@Entity(tableName = genresTableContract.NAME)
data class GenresEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = genresTableContract.COLUMN_NAME_ID)
    val id: Long? = null,
    @ColumnInfo(name = genresTableContract.COLUMN_NAME_NAME)
    val name: String
)