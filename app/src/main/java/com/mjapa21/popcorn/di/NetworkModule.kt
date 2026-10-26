package com.mjapa21.popcorn.di

import com.mjapa21.popcorn.data.movies.MovieApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://www.omdbapi.com/"

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<MovieApi> { get<Retrofit>().create(MovieApi::class.java) }
}