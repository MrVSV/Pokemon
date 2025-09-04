package com.vsv.pokemon.data.local_db

import androidx.room.ColumnInfo
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
    val hp: Int = 0,
    val attack: Int = 0,
    val defense: Int = 0,
    @ColumnInfo(name = "special_attack")
    val specialAttack: Int = 0,
    @ColumnInfo(name = "special_defense")
    val specialDefense: Int = 0,
    val speed: Int = 0,
)
