package com.vsv.pokemon.data.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val image: String,
    val order: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
)
