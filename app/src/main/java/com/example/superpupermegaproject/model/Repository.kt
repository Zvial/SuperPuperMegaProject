package com.example.superpupermegaproject.model

import android.util.Log
import com.example.superpupermegaproject.BuildConfig
import com.example.superpupermegaproject.model.api_responses.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class Repository(private val apiKey: String) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val handlerException = CoroutineExceptionHandler{ context, throwable ->
        Log.d(this.javaClass.canonicalName, throwable.localizedMessage)
        throwable.printStackTrace()
    }
    private val remoteRequestsCoroutineContext = Dispatchers.IO + SupervisorJob() + handlerException
    private lateinit var remoteApi: RESTApi
    private var imageURL: String = ""
    private var backdropSize: String = "original"
    private var posterSize: String = "original"

    init{
        initRetrofit()

        CoroutineScope(remoteRequestsCoroutineContext).launch {
            loadConfig()
        }
    }

    suspend fun loadMovies(page: Int): List<MovieItemResponse> {
        val moviesList = mutableListOf<MovieItemResponse>()
        var response: MoviesListResponse? = null
        withContext(remoteRequestsCoroutineContext) {
            response = remoteApi.getMovies(page = page + 1)
        }

        response?.let { movieResponse ->
            moviesList.clear()
            moviesList.addAll(movieResponse.results)
            moviesList.forEach { movie ->
                movie.backdropPath = applyImagePath(movie.backdropPath, backdropSize)
                movie.posterPath = applyImagePath(movie.posterPath, posterSize)
            }
        }

        return moviesList
    }

    suspend fun loadMovie(id: Int): MovieDetailResponse =
        withContext(remoteRequestsCoroutineContext) {
            remoteApi.getMovie(id).apply {
                backdropPath = applyImagePath(backdropPath, backdropSize)
                posterPath?.let {
                    posterPath = applyImagePath(it, posterSize)
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
            response = remoteApi.getGenres()
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
        val configurationResponse = remoteApi.getConfiguration()

        imageURL = configurationResponse.images.secureBaseURL
        backdropSize = configurationResponse.images.backdropSizes[1]
        posterSize = configurationResponse.images.posterSizes[1]
    }

    private fun applyImagePath(imagePath: String?, sizeString: String = "original") =
        if(imagePath==null) null
        else imageURL + sizeString + imagePath

}

class ApiKeyInterceptor(val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}