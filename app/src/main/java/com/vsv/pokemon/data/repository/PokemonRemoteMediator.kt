package com.vsv.pokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vsv.pokemon.data.local_db.PokemonDataBase
import com.vsv.pokemon.data.local_db.PokemonEntity
import com.vsv.pokemon.data.remote_api.PokemonApi
import com.vsv.pokemon.data.remote_api.safeCall
import com.vsv.pokemon.domain.mappers.toEntity
import com.vsv.pokemon.domain.model.onError
import com.vsv.pokemon.domain.model.onSuccess
import com.vsv.pokemon.domain.utils.LIMIT
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

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
            pokemonDB.pokemonDao().insertAllPokemons(
                response.results.asyncMap { pokemon ->
                    lateinit var pokemonDetails: PokemonEntity
                    safeCall { pokemonApi.getPokemonDetails(pokemon.name) }
                        .onSuccess { data ->
                            pokemonDetails = data.toEntity()
                        }
                        .onError {
                            pokemonDetails = pokemon.toEntity()
                        }
                    pokemonDetails
                }
            )
            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}

private suspend fun <T, R> List<T>.asyncMap(transform: suspend (T) -> R): List<R> {
    return coroutineScope {
        this@asyncMap.map { item ->
            async { transform(item) }
        }.awaitAll()
    }
}