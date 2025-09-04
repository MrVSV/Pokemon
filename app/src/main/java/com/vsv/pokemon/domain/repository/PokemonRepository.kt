package com.vsv.pokemon.domain.repository

import androidx.paging.PagingData
import com.vsv.pokemon.domain.model.PokemonModel
import com.vsv.pokemon.domain.model.PokemonTypeModel
import com.vsv.pokemon.domain.model.Result
import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.domain.model.errors.RemoteError
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonList(): Flow<PagingData<PokemonModel>>

    suspend fun searchPokemon(searchQuery: String): List<PokemonModel>

    suspend fun filterPokemons(searchQuery: String, sortParam: SortParam, types: List<String>): List<PokemonModel>

    suspend fun getPokemonTypes(): Result<List<PokemonTypeModel>, RemoteError>

//    suspend fun filterPokemonsByType(types: List<String>): List<PokemonModel>
}