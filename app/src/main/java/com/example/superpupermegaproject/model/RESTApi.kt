package com.example.superpupermegaproject.model

import com.example.superpupermegaproject.BuildConfig
import com.example.superpupermegaproject.model.api_responses.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RESTApi {

    @GET("configuration")
    suspend fun getConfiguration(@Query("api_key") apiKey: String = BuildConfig.API_KEY): ConfigurationResponse

    @GET("movie/{list_type}?language=ru")
    suspend fun getMovies(
        @Path("list_type") listType: String = "popular",
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int
    ): MoviesListResponse

    @GET("genre/movie/list?language=ru")
    suspend fun getGenres(@Query("api_key") apiKey: String = BuildConfig.API_KEY): GenresResponse

    @GET("movie/{movie_id}?language=ru")
    suspend fun getMovie(
        @Path("movie_id") movieID: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieDetailResponse

    @GET("movie/{movie_id}/credits?language=ru")
    suspend fun getMovieActors(
        @Path("movie_id") movieID: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): CreditsListResponse

}