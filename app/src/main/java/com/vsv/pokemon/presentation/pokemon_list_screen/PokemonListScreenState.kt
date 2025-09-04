package com.vsv.pokemon.presentation.pokemon_list_screen

import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.domain.model.LoadingStatus
import com.vsv.pokemon.presentation.ui_model.PokemonTypeUiModel
import com.vsv.pokemon.presentation.ui_model.PokemonUiModel


data class PokemonListScreenState(
    val loadingStatus: LoadingStatus = LoadingStatus.DEFAULT,
    val searchQuery: String = "",
    val pokemonsList: List<PokemonUiModel> = emptyList(),
    val isFiltersApplied: Boolean = false,
    val sortParam: SortParam = SortParam.DEFAULT,
    val pokemonTypes: List<PokemonTypeUiModel> = emptyList()
)
