package com.vsv.pokemon.data.local_db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PokemonTypeDao {

    @Upsert
    suspend fun insertAllPokemonTypes(types: List<PokemonTypeEntity>)

    @Query("SELECT * FROM pokemon_types")
    suspend fun getAllPokemonTypes(): List<PokemonTypeEntity>

}