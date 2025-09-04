package com.vsv.pokemon.data.local_db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vsv.pokemon.domain.model.SortParam

@Dao
interface PokemonDao {

    @Upsert
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Upsert
    suspend fun insertAllPokemons(pokemons: List<PokemonEntity>)

    @Query("SELECT * FROM pokemons")
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemons WHERE name LIKE :searchQuery || '%'")
    suspend fun searchPokemons(searchQuery: String): List<PokemonEntity>

    @Query("SELECT * FROM pokemons WHERE name = :pokemonName")
    suspend fun getPokemonByName(pokemonName: String): PokemonEntity?

    @Query("SELECT * FROM pokemons WHERE name LIKE :searchQuery || '%'")
    suspend fun filterPokemons(
        searchQuery: String,
    ): List<PokemonEntity>

    @Query(
        "SELECT * FROM pokemons WHERE name LIKE :searchQuery || '%' " +
                "ORDER BY " +
                "CASE WHEN :sortBy = 'name' THEN name END ASC, " +
                "CASE WHEN :sortBy = 'order' THEN `order` END ASC," +
                "CASE WHEN :sortBy = 'height' THEN height END DESC," +
                "CASE WHEN :sortBy = 'weight' THEN weight END DESC"
    )
    suspend fun filterAndSortPokemons(
        searchQuery: String,
        sortBy: String
    ): List<PokemonEntity>

    suspend fun getPokemonsWithQuery(
        searchQuery: String,
        sortParam: SortParam
    ): List<PokemonEntity> {
        return when(sortParam){
            SortParam.DEFAULT -> searchPokemons(searchQuery)
            else -> filterAndSortPokemons(searchQuery, sortParam.columnName)
        }
    }
}