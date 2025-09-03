package com.vsv.pokemon.domain.mappers

import androidx.core.net.toUri
import com.vsv.pokemon.data.local_db.PokemonEntity
import com.vsv.pokemon.data.remote_api.PokemonDto
import com.vsv.pokemon.domain.model.PokemonModel
import com.vsv.pokemon.presentation.ui_model.LoadingStatus
import com.vsv.pokemon.presentation.ui_model.PokemonColor
import com.vsv.pokemon.presentation.ui_model.PokemonUiModel

fun PokemonModel.toUiModel() = PokemonUiModel(
    name = name,
    imageUrl = image,
    color = PokemonColor.fromString(color),
    loadingStatus = if (PokemonColor.fromString(color) == null) LoadingStatus.LOADING else LoadingStatus.SUCCESS
)

fun PokemonDto.toModel() = PokemonModel(
    name = name,
    color = color?.name ?: "",
    image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${url?.toUri()?.lastPathSegment}.png"
)

fun PokemonDto.toEntity(image: String = ""): PokemonEntity {
    return PokemonEntity(
        name = name,
        color = color?.name ?: "",
        image = image,
    )
}

fun PokemonEntity.toModel() = PokemonModel(
    name = name,
    color = color ?: "",
    image = image
)