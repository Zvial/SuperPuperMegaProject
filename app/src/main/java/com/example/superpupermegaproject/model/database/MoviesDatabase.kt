package com.example.superpupermegaproject.model.database.entities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.superpupermegaproject.model.database.dao.ActorsDAO
import com.example.superpupermegaproject.model.database.dao.GenresDAO
import com.example.superpupermegaproject.model.database.dao.MoviesDAO

@Database(
        entities = arrayOf(
                ActorsEntity::class,
                GenresEntity::class,
                MovieEntity::class,
                MoviesActorsEntity::class,
                MoviesGenresEntity::class
        ),
        version = MoviesDatabase.Companion.DatabaseContract.database.VERSION
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract val moviesDAO: MoviesDAO
    abstract val actorsDAO: ActorsDAO
    abstract val genresDAO: GenresDAO

    companion object {
        @Volatile
        private var instance: MoviesDatabase? = null

        fun getInstance(applicationContext: Context): MoviesDatabase {
            synchronized(this) {
                var tmpInstance = instance
                if (tmpInstance == null) {
                    tmpInstance = Room.databaseBuilder(
                            applicationContext,
                            MoviesDatabase::class.java,
                            DatabaseContract.database.NAME
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    instance = tmpInstance
                }
                return tmpInstance
            }
        }

        object DatabaseContract {
            object database {
                const val NAME = "movies_database"
                const val VERSION = 1
            }

            object moviesTable {
                const val NAME = "movies"

                const val COLUMN_NAME_ID = "_id"
                const val COLUMN_NAME_MOVIE_ID = "movie_id"
                const val COLUMN_NAME_POSITION = "position"
                const val COLUMN_NAME_TITLE = "title"
                const val COLUMN_NAME_OVERVIEW = "overview"
                const val COLUMN_NAME_POSTER = "poster"
                const val COLUMN_NAME_BACKDROP = "backdrop"
                const val COLUMN_NAME_RATINGS = "ratings"
                const val COLUMN_NAME_ADULT = "adult"
                const val COLUMN_NAME_RUNTIME = "runtime"
                const val COLUMN_NAME_VOTECOUNT = "vote_count"
                const val COLUMN_NAME_ISLIKED = "liked"
            }

            object genresTable {
                const val NAME = "genres"

                const val COLUMN_NAME_ID = "_id"
                const val COLUMN_NAME_MOVIEID = "movie_id"
                const val COLUMN_NAME_NAME = "name"
            }

            object actorsTable {
                const val NAME = "actors"

                const val COLUMN_NAME_ID = "_id"
                const val COLUMN_NAME_MOVIEID = "movie_id"
                const val COLUMN_NAME_NAME = "name"
                const val COLUMN_NAME_PICTUREPATH = "picture_path"
            }

            object moviesActorsTable {
                const val NAME = "movies_actors"

                const val COLUMN_NAME_ACTORID = "actor_id"
                const val COLUMN_NAME_MOVIEID = "movie_id"
            }

            object moviesGenresTable {
                const val NAME = "movies_genres"

                const val COLUMN_NAME_GENREID = "genre_id"
                const val COLUMN_NAME_MOVIEID = "movie_id"
            }
        }
    }

}