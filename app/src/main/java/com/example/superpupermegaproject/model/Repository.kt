package com.example.superpupermegaproject.model

import android.util.Log
import com.example.superpupermegaproject.BuildConfig
import com.example.superpupermegaproject.model.api_responses.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class Repository(private val apiKey: String) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val handlerException = CoroutineExceptionHandler { context, throwable ->
        Log.d(this.javaClass.canonicalName, throwable.localizedMessage)
        throwable.printStackTrace()
    }
    private val remoteRequestsCoroutineContext = Dispatchers.IO + SupervisorJob() + handlerException
    private lateinit var remoteApi: RESTApi

    private var imageURL: String = "" //URL для загрузки картинок, полученный из config запроса
    private var backdropSize: String = "original"
    private var posterSize: String = "original"
    var lastPage = 0
        private set

    init {
        initRetrofit()

        CoroutineScope(remoteRequestsCoroutineContext).launch {
            loadConfig()
        }
    }

    fun clearLastPage() {
        lastPage = 0
    }

    suspend fun searchMovies(query: String): List<MovieItemResponse>{
        val moviesList = mutableListOf<MovieItemResponse>()

        withContext(remoteRequestsCoroutineContext) {
            try {
                val response = remoteApi.searchMovies(query = query, page = lastPage + 1)
                lastPage = response.page.toInt()
                moviesList.addAll(response.results)
                moviesList.forEach { movie ->
                    movie.backdropPath = applyImagePath(movie.backdropPath, backdropSize)
                    movie.posterPath = applyImagePath(movie.posterPath, posterSize)
                }
            } catch (t: Throwable) {
                Log.d("LOG", t.localizedMessage)
                throw t
            }
        }

        return moviesList
    }

    suspend fun loadMovies(): List<MovieItemResponse> {
        val moviesList = mutableListOf<MovieItemResponse>()

        withContext(remoteRequestsCoroutineContext) {
            try {
                val response = remoteApi.getMovies(page = lastPage + 1)
                lastPage = response.page.toInt()
                moviesList.addAll(response.results)
                moviesList.forEach { movie ->
                    movie.backdropPath = applyImagePath(movie.backdropPath, backdropSize)
                    movie.posterPath = applyImagePath(movie.posterPath, posterSize)
                }
            } catch (t: Throwable) {
                Log.d("LOG", t.localizedMessage)
                throw t
            }
        }

        return moviesList
    }

    suspend fun loadMovie(id: Int): MovieDetailResponse? =
        withContext(remoteRequestsCoroutineContext) {
            lastPage = 0
            try {
                remoteApi.getMovie(id).apply {
                    backdropPath = applyImagePath(backdropPath, backdropSize)
                    posterPath = applyImagePath(posterPath, posterSize)
                }
            } catch (t: Throwable) {
                Log.d(this.javaClass.canonicalName, t.localizedMessage)
                null
            }
        }

    suspend fun loadMovieActors(movieID: Int): List<CastResponse> =
        withContext(remoteRequestsCoroutineContext) {
            try {
                remoteApi.getMovieActors(movieID).cast.apply {
                    forEach { castResponse ->
                        castResponse.profilePath?.let {
                            castResponse.profilePath = applyImagePath(it)
                        }
                    }
                }
            } catch (t: Throwable) {
                Log.d(this.javaClass.canonicalName, t.localizedMessage)
                emptyList<CastResponse>()
            }
        }

    suspend fun loadGenres(): List<GenreResponse> {
        val genresList = mutableListOf<GenreResponse>()
        var response: GenresResponse? = null
        withContext(remoteRequestsCoroutineContext) {
            try {
                response = remoteApi.getGenres()
            } catch (t: Throwable) {
                Log.d(this.javaClass.canonicalName, t.localizedMessage)
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
            .addInterceptor(ApiKeyInterceptor(apiKey))
            .build()

        val retrofitInstance = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()

        remoteApi = retrofitInstance.create(RESTApi::class.java)
    }

    private suspend fun loadConfig() {
        try {
            val configurationResponse = remoteApi.getConfiguration()

            imageURL = configurationResponse.images.secureBaseURL
            backdropSize = configurationResponse.images.backdropSizes[1]
            posterSize = configurationResponse.images.posterSizes[1]
        } catch (t: Throwable) {
            Log.d(this.javaClass.canonicalName, t.localizedMessage)
        }
    }

    private fun applyImagePath(imagePath: String?, sizeString: String = "original") =
            if (imagePath == null)
                null
            else
                imageURL + sizeString + imagePath

}

