package com.vsv.pokemon.data.remote_api.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpritesDto(
    @Json(name = "front_default")
    val frontDefault: String,
)