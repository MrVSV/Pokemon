package com.vsv.pokemon.data.remote_api.dto


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonTypeListResponse(
    val results: List<TypeNameDto>
)

@JsonClass(generateAdapter = true)
data class TypeDto(
    val type: TypeNameDto
)

@JsonClass(generateAdapter = true)
data class TypeNameDto(
    val name: String,
)