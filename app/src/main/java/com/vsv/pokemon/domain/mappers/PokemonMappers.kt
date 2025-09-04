package com.vsv.pokemon.domain.mappers

import com.vsv.pokemon.data.local_db.PokemonEntity
import com.vsv.pokemon.data.remote_api.dto.PokemonDto
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
    hp = stats.find { it.statName.name == "hp" }?.baseStat ?: 0,
    attack = stats.find { it.statName.name == "attack" }?.baseStat ?: 0,
    defense = stats.find { it.statName.name == "defense" }?.baseStat ?: 0,
    specialAttack = stats.find { it.statName.name == "special-attack" }?.baseStat ?: 0,
    specialDefense = stats.find { it.statName.name == "special-defense" }?.baseStat ?: 0,
    speed = stats.find { it.statName.name == "speed" }?.baseStat ?: 0,
)

fun PokemonEntity.toModel() = PokemonModel(
    name = name,
    image = image,
    order = order,
    height = height,
    weight = weight
)