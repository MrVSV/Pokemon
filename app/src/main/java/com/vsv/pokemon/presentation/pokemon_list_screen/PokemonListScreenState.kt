package com.vsv.pokemon.presentation.pokemon_list_screen

import com.vsv.pokemon.presentation.ui_model.PokemonUiModel


data class PokemonListScreenState(
    val searchQuery: String = "",
    val pokemonsList: List<PokemonUiModel> = emptyList()
)
