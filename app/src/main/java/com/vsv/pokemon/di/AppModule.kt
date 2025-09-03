package com.vsv.pokemon.di

import androidx.room.Room
import com.vsv.pokemon.data.local_db.PokemonDataBase
import com.vsv.pokemon.data.remote_api.PokemonApi
import com.vsv.pokemon.data.repository.PokemonRepositoryImpl
import com.vsv.pokemon.domain.repository.PokemonRepository
import com.vsv.pokemon.presentation.pokemon_list_screen.PokemonListScreenViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://pokeapi.co/api/v2/"
const val TIMEOUT_SECONDS = 20L

val appModule = module {

    viewModelOf(::PokemonListScreenViewModel)

    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    single<PokemonApi> { get<Retrofit>().create() }

    single { get<PokemonDataBase>().pokemonDao() }

    single {
        Room.databaseBuilder(
            context = get(),
            klass = PokemonDataBase::class.java,
            name = "db"
        ).build()
    }

}