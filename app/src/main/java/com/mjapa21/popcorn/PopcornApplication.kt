package com.mjapa21.popcorn

import android.app.Application
import com.mjapa21.popcorn.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PopcornApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PopcornApplication)
            modules(
                viewModelsModule
            )
        }
    }
}