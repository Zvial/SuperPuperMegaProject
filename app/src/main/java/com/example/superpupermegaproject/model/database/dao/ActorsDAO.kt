package com.example.superpupermegaproject.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.superpupermegaproject.model.database.entities.ActorsEntity
import com.example.superpupermegaproject.model.database.entities.MoviesDatabase

@Dao
interface ActorsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: ActorsEntity): Long

    @Query(
        "DELETE " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.actorsTable.NAME} " +
                "WHERE " +
                "   ${MoviesDatabase.Companion.DatabaseContract.actorsTable.COLUMN_NAME_ID} = :id"
    )
    suspend fun delete(id: Long)

    @Query(
        "SELECT " +
                "   * " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.actorsTable.NAME} " +
                "WHERE " +
                "   ${MoviesDatabase.Companion.DatabaseContract.actorsTable.COLUMN_NAME_ID} = :id "
    )
    suspend fun getActor(id: Long): ActorsEntity

    /*@Query(
        "SELECT " +
                "   * " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.actorsTable.NAME} " +
                "WHERE " +
                "   ${MoviesDatabase.Companion.DatabaseContract.actorsTable.COLUMN_NAME_MOVIEID} = :movieID"
    )
    suspend fun getMovieActors(movieID: Long): List<ActorsEntity>*/
}