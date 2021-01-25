package com.example.superpupermegaproject.model.database.dao

import androidx.room.*
import com.example.superpupermegaproject.model.database.entities.ActorsEntity
import com.example.superpupermegaproject.model.database.entities.GenresEntity
import com.example.superpupermegaproject.model.database.entities.MoviesDatabase

@Dao
interface GenresDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genre: GenresEntity): Long

    @Transaction
    suspend fun insertAll(genres: List<GenresEntity>) {
        genres.forEach {
            insert(it)
        }
    }

    @Query(
        "DELETE " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.genresTable.NAME} " +
                "WHERE " +
                "   ${MoviesDatabase.Companion.DatabaseContract.genresTable.COLUMN_NAME_ID} = :id"
    )
    suspend fun delete(id: Long)

    @Query(
        "SELECT " +
                "   * " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.genresTable.NAME} " +
                "WHERE " +
                "   ${MoviesDatabase.Companion.DatabaseContract.genresTable.COLUMN_NAME_ID} = :id "
    )
    suspend fun getGenre(id: Long): GenresEntity

    @Query(
            "SELECT " +
                    "   * " +
                    "FROM " +
                    "   ${MoviesDatabase.Companion.DatabaseContract.genresTable.NAME} "
    )
    suspend fun getAll(): List<GenresEntity>

    @Query(
        "SELECT " +
                "   genres.* " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.genresTable.NAME} as genres " +
                "   INNER JOIN ${MoviesDatabase.Companion.DatabaseContract.moviesGenresTable.NAME} as movie_genres " +
                "       ON genres.${MoviesDatabase.Companion.DatabaseContract.genresTable.COLUMN_NAME_ID} " +
                "           = movie_genres.${MoviesDatabase.Companion.DatabaseContract.moviesGenresTable.COLUMN_NAME_GENREID} " +
                "WHERE " +
                "   ${MoviesDatabase.Companion.DatabaseContract.moviesGenresTable.COLUMN_NAME_MOVIEID} = :movieID"
    )
    suspend fun getMovieGenres(movieID: Long): List<GenresEntity>
}