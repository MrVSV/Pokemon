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
import com.vsv.pokemon.domain.model.PokemonTypeModel
import com.vsv.pokemon.domain.model.Result
import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.domain.model.errors.RemoteError
import com.vsv.pokemon.domain.model.map
import com.vsv.pokemon.domain.model.onSuccess
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
        sortParam: SortParam,
        types: List<String>
    ): List<PokemonModel> {
        return pokemonDB.pokemonDao().filterAndSortPokemons(searchQuery, sortParam.columnName, types)
            .map { it.toModel() }
    }

    override suspend fun getPokemonTypes(): Result<List<PokemonTypeModel>, RemoteError> {
        val pokemonTypes = pokemonDB.pokemonTypeDao().getAllPokemonTypes()
        if (pokemonTypes.isNotEmpty()) {
            return Result.Success(pokemonTypes.map { it.toModel() })
        } else {
            val resp = safeCall { pokemonApi.getPokemonTypeList() }
                .onSuccess {
                    pokemonDB.pokemonTypeDao()
                        .insertAllPokemonTypes(it.results.map { type -> type.toEntity() })
                }
            return resp.map { it.results.map { type -> type.toEntity().toModel() } }
        }
    }

}