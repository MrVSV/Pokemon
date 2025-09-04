package com.vsv.pokemon.data.local_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "pokemon_type_cross_ref",
    primaryKeys = ["pokemon_name", "type_name"],
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["name"],
            childColumns = ["pokemon_name"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PokemonTypeEntity::class,
            parentColumns = ["name"],
            childColumns = ["type_name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PokemonTypeCrossRef(
    @ColumnInfo("pokemon_name")
    val pokemonName: String,
    @ColumnInfo("type_name")
    val typeName: String,
)
