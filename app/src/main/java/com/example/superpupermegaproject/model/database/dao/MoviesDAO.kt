package com.example.superpupermegaproject.model.database.dao

import androidx.room.*
import com.example.superpupermegaproject.model.database.entities.MovieEntity
import com.example.superpupermegaproject.model.database.entities.MoviesDatabase
import com.example.superpupermegaproject.model.database.entities.MoviesGenresEntity

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenre(genre: MoviesGenresEntity): Long

    @Query("DELETE FROM ${MoviesDatabase.Companion.DatabaseContract.moviesTable.NAME}")
    suspend fun deleteAll()

    @Transaction
    suspend fun insertAll(movies: List<MovieEntity>, movieGenres: List<MoviesGenresEntity>) {
        movies.forEach { movie ->
            insert(movie)
        }
        movieGenres.forEach { movieGenre  ->
            insertMovieGenre(movieGenre)
        }
    }

    @Query(
        "SELECT " +
                "   * " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.moviesTable.NAME} " +
                "ORDER BY " +
                "   ${MoviesDatabase.Companion.DatabaseContract.moviesTable.COLUMN_NAME_POSITION} "
    )
    suspend fun getAll(): List<MovieEntity>

    @Query(
            "SELECT " +
                    "   * " +
                    "FROM " +
                    "   ${MoviesDatabase.Companion.DatabaseContract.moviesTable.NAME} " +
                    "ORDER BY " +
                    "   ${MoviesDatabase.Companion.DatabaseContract.moviesTable.COLUMN_NAME_POSITION} " +
                    "LIMIT :firstRow, :rowsCount "
    )
    suspend fun getAllByPage(firstRow: Int, rowsCount: Int): List<MovieEntity>

    /*@Transaction
    @Query(
        "SELECT " +
                "   * " +
                "FROM " +
                "   ${MoviesDatabase.Companion.DatabaseContract.moviesTable.NAME}"
    )
    suspend fun getAllDataSource(): DataSource.Factory<Int, MovieEntity>*/

    @Transaction
    @Query(
        "SELECT " +
                "   * " +
                "FROM " +
                "${MoviesDatabase.Companion.DatabaseContract.moviesTable.NAME} " +
                "WHERE " +
                "   ${MoviesDatabase.Companion.DatabaseContract.moviesTable.COLUMN_NAME_ID} = :id "
    )
    suspend fun getMovie(id: Long): MovieEntity
}