package com.vsv.pokemon.domain.mappers

import androidx.core.net.toUri
import com.vsv.pokemon.data.local_db.PokemonEntity
import com.vsv.pokemon.data.remote_api.dto.PokemonDto
import com.vsv.pokemon.data.remote_api.dto.PokemonListItemDto
import com.vsv.pokemon.domain.model.PokemonModel
import com.vsv.pokemon.presentation.ui_model.PokemonUiModel

fun PokemonModel.toUiModel() = PokemonUiModel(
    name = name,
    imageUrl = image,
    order = order,
    height = height,
    weight = weight,
)

fun PokemonDto.toEntity() = PokemonEntity(
    name = name,
    image = sprites.frontDefault,
    order = order,
    height = height,
    weight = weight,
)

fun PokemonListItemDto.toEntity(): PokemonEntity {
    return PokemonEntity(
        name = name,
        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${url?.toUri()?.lastPathSegment}.png",
    )
}

fun PokemonEntity.toModel() = PokemonModel(
    name = name,
    image = image,
    order = order,
    height = height,
    weight = weight
)