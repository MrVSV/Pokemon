package com.vsv.pokemon.data.remote_api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonListResponse(
    @Json(name = "count")
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonDto>
)


@JsonClass(generateAdapter = true)
data class PokemonDto(
    val name: String,
    val url: String?,
    val color: PokemonColorDto?,
)

@JsonClass(generateAdapter = true)
data class PokemonColorDto(
    val name: String
)