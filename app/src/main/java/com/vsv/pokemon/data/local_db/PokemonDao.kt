package com.vsv.pokemon.data.local_db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PokemonDao {

    @Upsert
    suspend fun insertAllPokemons(pokemons: List<PokemonEntity>)

    @Upsert
    suspend fun insertPokemonTypeCrossRef(crossRefs: PokemonTypeCrossRef)

    @Query("SELECT * FROM pokemons")
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemons WHERE name LIKE :searchQuery || '%'")
    suspend fun searchPokemons(searchQuery: String): List<PokemonEntity>

    @Query(
        "SELECT * FROM pokemons WHERE name LIKE :searchQuery || '%' " +
                "AND name IN( SELECT pokemon_name FROM pokemon_type_cross_ref WHERE type_name IN (:types))" +
                "ORDER BY " +
                "CASE WHEN :sortBy = 'name' THEN name END ASC, " +
                "CASE WHEN :sortBy = 'order' THEN `order` END ASC," +
                "CASE WHEN :sortBy = 'height' THEN height END DESC," +
                "CASE WHEN :sortBy = 'weight' THEN weight END DESC," +
                "CASE WHEN :sortBy = 'hp' THEN hp END DESC," +
                "CASE WHEN :sortBy = 'attack' THEN attack END DESC," +
                "CASE WHEN :sortBy = 'defense' THEN defense END DESC," +
                "CASE WHEN :sortBy = 'special_attack' THEN special_attack END DESC," +
                "CASE WHEN :sortBy = 'special_defense' THEN special_defense END DESC," +
                "CASE WHEN :sortBy = 'speed' THEN speed END DESC"
    )
    suspend fun filterAndSortPokemons(
        searchQuery: String,
        sortBy: String,
        types: List<String>,
    ): List<PokemonEntity>
}