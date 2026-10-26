package com.mjapa21.popcorn.di

import com.mjapa21.popcorn.data.AuthRepositoryImpl
import com.mjapa21.popcorn.domain.repository.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}