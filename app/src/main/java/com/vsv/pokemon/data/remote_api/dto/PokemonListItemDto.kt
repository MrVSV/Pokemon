package com.vsv.pokemon.data.remote_api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonListResponse(
    @Json(name = "count")
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemDto>
)


@JsonClass(generateAdapter = true)
data class PokemonListItemDto(
    val name: String,
    val url: String?,
)

@JsonClass(generateAdapter = true)
data class PokemonColorDto(
    val name: String
)