package com.vsv.pokemon.data.remote_api.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDto(
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "order")
    val order: Int,
    @Json(name = "sprites")
    val sprites: SpritesDto,
    @Json(name = "stats")
    val stats: List<StatDto>,
    @Json(name = "types")
    val types: List<TypeDto>,
    @Json(name = "weight")
    val weight: Int
)