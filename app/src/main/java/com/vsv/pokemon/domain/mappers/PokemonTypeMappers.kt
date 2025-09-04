package com.vsv.pokemon.domain.mappers

import com.vsv.pokemon.data.local_db.PokemonTypeEntity
import com.vsv.pokemon.data.remote_api.dto.TypeNameDto
import com.vsv.pokemon.domain.model.PokemonTypeModel
import com.vsv.pokemon.presentation.ui_model.PokemonTypeUiModel

fun PokemonTypeModel.toUiModel() = PokemonTypeUiModel(
    name = name
)

fun TypeNameDto.toEntity() = PokemonTypeEntity(
    name = name
)

fun PokemonTypeEntity.toModel() = PokemonTypeModel(
    name = name
)