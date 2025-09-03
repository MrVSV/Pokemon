package com.vsv.pokemon.presentation.pokemon_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vsv.pokemon.domain.mappers.toUiModel
import com.vsv.pokemon.domain.model.onError
import com.vsv.pokemon.domain.model.onSuccess
import com.vsv.pokemon.domain.repository.PokemonRepository
import com.vsv.pokemon.presentation.ui_model.LoadingStatus
import com.vsv.pokemon.presentation.ui_model.PokemonUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PokemonListScreenViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListScreenState())
    val state = _state.asStateFlow()

    private val _pokemonPagingData =
        MutableStateFlow<PagingData<PokemonUiModel>>(PagingData.empty())
    val pokemonPagingData = _pokemonPagingData.asStateFlow()


    fun onEvent(event: PokemonListScreenEvent) {
        when (event) {
            PokemonListScreenEvent.GetPokemons -> getPokemons()
            is PokemonListScreenEvent.GetPokemonSpecies -> getPokemonSpecies(event.pokemonName)
            is PokemonListScreenEvent.OnSearchValueChange -> onSearchQueryChange(event.query)
        }
    }

    private fun getPokemons() {
        viewModelScope.launch {
            pokemonRepository.getPokemonList().cachedIn(viewModelScope).collect {
                _pokemonPagingData.value = it.map { pokemon -> pokemon.toUiModel() }
            }
        }
    }

    private fun getPokemonSpecies(pokemonName: String) {
        viewModelScope.launch {
            _pokemonPagingData.update { data ->
                data.map { pokemon ->
                    if (pokemon.name == pokemonName) {
                        pokemon.copy(
                            color = null,
                            loadingStatus = LoadingStatus.LOADING
                        )
                    } else {
                        pokemon
                    }
                }
            }
            pokemonRepository.getPokemonSpecies(pokemonName)
                .onSuccess { pokemon ->
                    val newPokemon = pokemon.toUiModel()
                    _pokemonPagingData.update { data ->
                        data.map {
                            if (it.name == pokemonName) {
                                it.copy(
                                    color = newPokemon.color,
                                    loadingStatus = LoadingStatus.SUCCESS
                                )
                            } else {
                                it
                            }
                        }
                    }
                }
                .onError {
                    _pokemonPagingData.update { data ->
                        data.map { pokemon ->
                            if (pokemon.name == pokemonName) {
                                pokemon.copy(
                                    color = null,
                                    loadingStatus = LoadingStatus.ERROR
                                )
                            } else {
                                pokemon
                            }
                        }
                    }
                }
        }
    }

    private fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    pokemonsList = pokemonRepository.searchPokemon(query).map { pokemon ->
                        pokemon.toUiModel().copy(loadingStatus = LoadingStatus.SUCCESS)
                    }
                )
            }
        }
    }

}