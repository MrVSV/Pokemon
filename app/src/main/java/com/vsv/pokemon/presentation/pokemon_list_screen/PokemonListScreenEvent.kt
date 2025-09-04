package com.vsv.pokemon.presentation.pokemon_list_screen

import com.vsv.pokemon.domain.model.SortParam

sealed interface PokemonListScreenEvent {
    data object GetPokemons: PokemonListScreenEvent
    data class OnSearchValueChange(val query: String): PokemonListScreenEvent
    data class OnSortParamChange(val param: SortParam): PokemonListScreenEvent
    data object OnApplyFilters: PokemonListScreenEvent

}