package com.mjapa21.popcorn.di

import com.mjapa21.popcorn.data.authentication.AuthRepositoryImpl
import com.mjapa21.popcorn.data.movies.MovieRepositoryImpl
import com.mjapa21.popcorn.domain.repository.AuthRepository
import com.mjapa21.popcorn.domain.repository.MovieRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}