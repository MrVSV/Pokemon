package com.vsv.pokemon.data.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val color: String?,
    val image: String,
)
