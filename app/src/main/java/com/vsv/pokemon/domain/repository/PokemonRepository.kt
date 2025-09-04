package com.vsv.pokemon.domain.repository

import androidx.paging.PagingData
import com.vsv.pokemon.domain.model.PokemonModel
import com.vsv.pokemon.domain.model.SortParam
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemonList(): Flow<PagingData<PokemonModel>>

    suspend fun searchPokemon(searchQuery: String): List<PokemonModel>

    suspend fun filterPokemons(searchQuery: String, sortParam: SortParam): List<PokemonModel>
}