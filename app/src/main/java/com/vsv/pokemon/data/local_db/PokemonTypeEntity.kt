package com.vsv.pokemon.data.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_types")
data class PokemonTypeEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String
)
