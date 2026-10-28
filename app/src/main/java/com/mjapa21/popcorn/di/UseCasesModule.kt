package com.mjapa21.popcorn.di

import com.mjapa21.popcorn.domain.usecase.GetMovieDetailsUseCase
import com.mjapa21.popcorn.domain.usecase.LoginUseCase
import com.mjapa21.popcorn.domain.usecase.SearchMoviesUseCase
import com.mjapa21.popcorn.domain.usecase.SignUpUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCasesModule = module {
    factoryOf(::LoginUseCase)
    factoryOf(::SignUpUseCase)
    factoryOf(::SearchMoviesUseCase)
    factoryOf(::GetMovieDetailsUseCase)
}