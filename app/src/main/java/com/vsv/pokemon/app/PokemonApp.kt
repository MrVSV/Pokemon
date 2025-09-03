package com.vsv.pokemon.app

import android.app.Application
import com.vsv.pokemon.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PokemonApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(androidContext = this@PokemonApp)
            modules(appModule)
        }
    }
}