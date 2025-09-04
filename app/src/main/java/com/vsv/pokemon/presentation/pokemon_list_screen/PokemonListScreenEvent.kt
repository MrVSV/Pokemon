package com.vsv.pokemon.presentation.pokemon_list_screen

import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.presentation.ui_model.PokemonTypeUiModel

sealed interface PokemonListScreenEvent {
    data object GetPokemons: PokemonListScreenEvent
    data class OnSearchValueChange(val query: String): PokemonListScreenEvent
    data class OnSortParamChange(val param: SortParam): PokemonListScreenEvent
    data class OnPokemonTypeSelected(val type: PokemonTypeUiModel): PokemonListScreenEvent
    data object OnApplyFilters: PokemonListScreenEvent

}