package com.example.superpupermegaproject.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

internal val actorsTableContract = MoviesDatabase.Companion.DatabaseContract.actorsTable

@Entity(tableName = actorsTableContract.NAME)
data class ActorsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = actorsTableContract.COLUMN_NAME_ID)
    val id: Long? = null,
    @ColumnInfo(name = actorsTableContract.COLUMN_NAME_NAME)
    val name: String,
    @ColumnInfo(name = actorsTableContract.COLUMN_NAME_PICTUREPATH)
    val picturePath: String
)