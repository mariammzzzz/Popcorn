package com.mjapa21.popcorn.di

import com.mjapa21.popcorn.presentation.pages.details.DetailsViewModel
import com.mjapa21.popcorn.presentation.pages.home.HomeViewModel
import com.mjapa21.popcorn.presentation.pages.login.LoginViewModel
import com.mjapa21.popcorn.presentation.pages.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::HomeViewModel)
    viewModel { (imdbId: String) ->
        DetailsViewModel(
            imdbId = imdbId,
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}