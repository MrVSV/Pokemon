package com.vsv.pokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.vsv.pokemon.data.local_db.PokemonDataBase
import com.vsv.pokemon.data.remote_api.PokemonApi
import com.vsv.pokemon.domain.mappers.toModel
import com.vsv.pokemon.domain.model.PokemonModel
import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.domain.repository.PokemonRepository
import com.vsv.pokemon.domain.utils.LIMIT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val pokemonApi: PokemonApi,
    private val pokemonDB: PokemonDataBase
) : PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPokemonList(): Flow<PagingData<PokemonModel>> {
        return Pager(
            config = PagingConfig(pageSize = LIMIT),
            remoteMediator = PokemonRemoteMediator(pokemonDB, pokemonApi),
            pagingSourceFactory = { pokemonDB.pokemonDao().getPokemons() }
        ).flow.map {
            it.map { entity ->
                entity.toModel()
            }
        }
    }

    override suspend fun searchPokemon(searchQuery: String): List<PokemonModel> {
        return pokemonDB.pokemonDao().searchPokemons(searchQuery).map { it.toModel() }
    }

    override suspend fun filterPokemons(
        searchQuery: String,
        sortParam: SortParam
    ): List<PokemonModel> {
        return pokemonDB.pokemonDao().getPokemonsWithQuery(searchQuery, sortParam).map { it.toModel() }
    }
}