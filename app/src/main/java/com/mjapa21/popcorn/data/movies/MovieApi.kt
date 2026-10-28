package com.mjapa21.popcorn.data.movies

import com.mjapa21.popcorn.data.model.MovieDetailsDto
import com.mjapa21.popcorn.data.model.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "1da091d2"

interface MovieApi {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") title: String,
        @Query("page") page: Int = 1,
        @Query("apikey") apiKey: String = API_KEY,
    ): SearchResponseDto


    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") id: String,
        @Query("apikey") apiKey: String = API_KEY
    ): MovieDetailsDto
}