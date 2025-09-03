package com.vsv.pokemon.data.repository

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vsv.pokemon.data.local_db.PokemonDataBase
import com.vsv.pokemon.data.local_db.PokemonEntity
import com.vsv.pokemon.data.remote_api.PokemonApi
import com.vsv.pokemon.domain.mappers.toEntity

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
            LoadType.APPEND -> (offset ?: 0) + 20
        } ?: return MediatorResult.Success(true)
        return try {
            val response = pokemonApi.getPokemonList(20, offset!!)
            pokemonDB.pokemonDao().insertAllPokemons(
                response.results.map {
                    val entity = pokemonDB.pokemonDao().getPokemonByName(it.name)
                    entity ?: it.toEntity(
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${it.url?.toUri()?.lastPathSegment}.png"
                    )
                }
            )
            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}