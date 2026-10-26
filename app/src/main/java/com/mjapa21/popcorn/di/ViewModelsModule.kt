package com.mjapa21.popcorn.di

import com.mjapa21.popcorn.presentation.pages.login.LoginViewModel
import com.mjapa21.popcorn.presentation.pages.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
}