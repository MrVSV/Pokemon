package com.vsv.pokemon.data.remote_api.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TypeDto(
    @Json(name = "type")
    val type: TypeNameDto
)

@JsonClass(generateAdapter = true)
data class TypeNameDto(
    @Json(name = "name")
    val name: String,
)