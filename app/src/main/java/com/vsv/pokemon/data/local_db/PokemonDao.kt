package com.vsv.pokemon.data.local_db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

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

    @Query("SELECT * FROM pokemons WHERE name = :pokemonName LIMIT 100")
    suspend fun getPokemonByName(pokemonName: String): PokemonEntity?

    @Query("DELETE FROM pokemons")
    suspend fun clearAll()

}