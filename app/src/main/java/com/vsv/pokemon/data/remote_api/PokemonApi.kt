package com.vsv.pokemon.data.remote_api

import com.vsv.pokemon.data.remote_api.dto.PokemonDto
import com.vsv.pokemon.data.remote_api.dto.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}/")
    suspend fun getPokemonDetails(
        @Path("name") name: String
    ): PokemonDto

}