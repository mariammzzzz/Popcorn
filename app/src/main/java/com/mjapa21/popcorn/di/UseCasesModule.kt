package com.mjapa21.popcorn.di

import com.mjapa21.popcorn.domain.usecase.authetication.LoginUseCase
import com.mjapa21.popcorn.domain.usecase.authetication.SignUpUseCase
import com.mjapa21.popcorn.domain.usecase.library.ObserveLibraryUseCase
import com.mjapa21.popcorn.domain.usecase.library.RemoveMovieUseCase
import com.mjapa21.popcorn.domain.usecase.library.SaveMovieUseCase
import com.mjapa21.popcorn.domain.usecase.library.UpdateMovieStatusUseCase
import com.mjapa21.popcorn.domain.usecase.movie.GetMovieDetailsUseCase
import com.mjapa21.popcorn.domain.usecase.movie.SearchMoviesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCasesModule = module {
    factoryOf(::LoginUseCase)
    factoryOf(::SignUpUseCase)

    factoryOf(::SearchMoviesUseCase)
    factoryOf(::GetMovieDetailsUseCase)

    factoryOf(::ObserveLibraryUseCase)
    factoryOf(::SaveMovieUseCase)
    factoryOf(::UpdateMovieStatusUseCase)
    factoryOf(::RemoveMovieUseCase)
}