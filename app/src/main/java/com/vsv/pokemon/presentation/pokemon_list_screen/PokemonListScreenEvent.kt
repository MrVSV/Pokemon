package com.vsv.pokemon.presentation.pokemon_list_screen

sealed interface PokemonListScreenEvent {
    data object GetPokemons: PokemonListScreenEvent
    data class GetPokemonSpecies(val pokemonName: String): PokemonListScreenEvent
    data class OnSearchValueChange(val query: String): PokemonListScreenEvent
}