package com.vsv.pokemon.data.remote_api.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatDto(
    @Json(name = "base_stat")
    val baseStat: Int,
    @Json(name = "stat")
    val statName: StatNameDto
)

@JsonClass(generateAdapter = true)
data class StatNameDto(
    @Json(name = "name")
    val name: String,
)