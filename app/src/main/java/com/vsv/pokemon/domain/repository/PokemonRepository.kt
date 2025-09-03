package com.vsv.pokemon.domain.repository

import androidx.paging.PagingData
import com.vsv.pokemon.domain.model.PokemonModel
import com.vsv.pokemon.domain.model.Result
import com.vsv.pokemon.domain.model.errors.RemoteError
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonList(): Flow<PagingData<PokemonModel>>

    suspend fun getPokemonSpecies(pokemonName: String): Result<PokemonModel, RemoteError>

    suspend fun searchPokemon(searchQuery: String): List<PokemonModel>
}