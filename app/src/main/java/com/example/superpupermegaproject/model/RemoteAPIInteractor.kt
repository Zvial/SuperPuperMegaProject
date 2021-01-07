package com.example.superpupermegaproject.model

import android.util.Log
import com.example.superpupermegaproject.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.lang.Exception

class RemoteAPIInteractor {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val remoteRequestsCoroutineContext = Dispatchers.IO
    private lateinit var retrofitInstance: Retrofit
    private lateinit var remoteApi: TmDBAPI
    private lateinit var configurationResponse: ConfigurationResponse
    private var imageURL: String = ""

    init{
        initRetrofit()

        remoteApi = retrofitInstance.create(TmDBAPI::class.java)

        CoroutineScope(remoteRequestsCoroutineContext).launch {
            loadConfig()
        }
    }

    suspend fun loadMovies(): List<MovieItemResponse> {
        val moviesList = mutableListOf<MovieItemResponse>()
        var response: MoviesListResponse? = null
        withContext(remoteRequestsCoroutineContext) {
            try {
                response = remoteApi.getMovies()
            } catch (e: Exception) {
                Log.d(this.javaClass.canonicalName, e.localizedMessage)
            }
        }

        response?.let { movieResponse ->
            moviesList.clear()
            moviesList.addAll(movieResponse.results)
            moviesList.forEach { movie ->
                movie.backdropPath = applyImagePath(movie.backdropPath)
                movie.posterPath = applyImagePath(movie.posterPath)
            }
        }

        return moviesList
    }

    suspend fun loadMovie(id: Int): MovieDetailResponse =
        withContext(remoteRequestsCoroutineContext) {
            remoteApi.getMovie(id).apply {
                backdropPath = applyImagePath(backdropPath)
                posterPath?.let {
                    posterPath = applyImagePath(it)
                }
            }
        }

    suspend fun loadMovieActors(movieID: Int): List<CastResponse> =
        withContext(remoteRequestsCoroutineContext) {
            remoteApi.getMovieActors(movieID).cast.apply {
                forEach { castResponse ->
                    castResponse.profilePath?.let {
                        castResponse.profilePath = applyImagePath(it)
                    }
                }
            }
        }

    suspend fun loadGenres(): List<GenreResponse> {
        val genresList = mutableListOf<GenreResponse>()
        var response: GenresResponse? = null
        withContext(remoteRequestsCoroutineContext) {
            try {
                response = remoteApi.getGenres()
            } catch (e: Exception) {
                Log.d(this.javaClass.canonicalName, e.localizedMessage)
            }
        }

        response?.let { genresResponse ->
            genresList.addAll(genresResponse.genres)
        }

        return genresList
    }

    private fun initRetrofit() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        retrofitInstance = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    private suspend fun loadConfig() {
        try {
            configurationResponse = remoteApi.getConfiguration()
        } catch (e: Exception) {
            Log.d(this.javaClass.canonicalName, e.localizedMessage)
        }

        imageURL = configurationResponse.images.secureBaseURL + "original"
    }

    private fun applyImagePath(imagePath: String) = imageURL + imagePath

}