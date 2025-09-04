package com.vsv.pokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vsv.pokemon.data.local_db.PokemonDataBase
import com.vsv.pokemon.data.local_db.PokemonEntity
import com.vsv.pokemon.data.local_db.PokemonTypeCrossRef
import com.vsv.pokemon.data.remote_api.PokemonApi
import com.vsv.pokemon.data.remote_api.dto.PokemonDto
import com.vsv.pokemon.data.remote_api.safeCall
import com.vsv.pokemon.domain.mappers.toEntity
import com.vsv.pokemon.domain.model.onSuccess
import com.vsv.pokemon.domain.utils.LIMIT
import com.vsv.pokemon.domain.utils.asyncMap

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val pokemonDB: PokemonDataBase,
    private val pokemonApi: PokemonApi,
) : RemoteMediator<Int, PokemonEntity>() {

    private var offset: Int? = 0
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> null
            LoadType.APPEND -> (offset ?: 0) + LIMIT
        } ?: return MediatorResult.Success(true)
        return try {
            val response = pokemonApi.getPokemonList(LIMIT, offset!!)
            val pokemonDetailsList = mutableListOf<PokemonDto>()
            response.results.asyncMap { pokemon ->
                safeCall { pokemonApi.getPokemonDetails(pokemon.name) }
                    .onSuccess { data ->
                        pokemonDetailsList.add(data)
                    }
            }
            pokemonDB.pokemonDao().insertAllPokemons(pokemonDetailsList.map { it.toEntity() })
            pokemonDetailsList.forEach { pokemon ->
                pokemon.types.forEach { type ->
                    pokemonDB.pokemonDao().insertPokemonTypeCrossRef(
                        PokemonTypeCrossRef(
                            pokemon.name,
                            type.type.name
                        )
                    )
                }
            }
            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

}

