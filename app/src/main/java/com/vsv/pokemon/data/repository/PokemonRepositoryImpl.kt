package com.vsv.pokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.vsv.pokemon.data.local_db.PokemonDataBase
import com.vsv.pokemon.data.remote_api.PokemonApi
import com.vsv.pokemon.data.remote_api.safeCall
import com.vsv.pokemon.domain.mappers.toEntity
import com.vsv.pokemon.domain.mappers.toModel
import com.vsv.pokemon.domain.model.PokemonModel
import com.vsv.pokemon.domain.model.Result
import com.vsv.pokemon.domain.model.errors.RemoteError
import com.vsv.pokemon.domain.model.map
import com.vsv.pokemon.domain.model.onSuccess
import com.vsv.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PokemonRepositoryImpl(
    private val pokemonApi: PokemonApi,
    private val pokemonDB: PokemonDataBase
): PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPokemonList(): Flow<PagingData<PokemonModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PokemonRemoteMediator(pokemonDB, pokemonApi),
            pagingSourceFactory = { pokemonDB.pokemonDao().getPokemons() }
        ).flow.map {
            it.map { entity ->
                entity.toModel()
            }
        }
    }

    override suspend fun getPokemonSpecies(pokemonName: String): Result<PokemonModel, RemoteError> {
        val resp = safeCall {  pokemonApi.getPokemonSpecies(pokemonName)}.onSuccess {
            withContext(Dispatchers.IO) {
                val savedPokemon = pokemonDB.pokemonDao().getPokemonByName(pokemonName)
                val image = savedPokemon?.image ?: ""
                pokemonDB.pokemonDao().insertPokemon(it.toEntity(image))
            }
        }
        return resp.map { it.toModel() }

    }

    override suspend fun searchPokemon(searchQuery: String): List<PokemonModel> {
        return pokemonDB.pokemonDao().searchPokemons(searchQuery).map { it.toModel() }
    }
}