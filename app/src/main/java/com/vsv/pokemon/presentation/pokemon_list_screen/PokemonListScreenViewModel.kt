package com.vsv.pokemon.presentation.pokemon_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vsv.pokemon.domain.mappers.toUiModel
import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.domain.repository.PokemonRepository
import com.vsv.pokemon.presentation.ui_model.PokemonUiModel
import kotlinx.coroutines.Dispatchers
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
            is PokemonListScreenEvent.OnSearchValueChange -> onSearchQueryChange(event.query)
            is PokemonListScreenEvent.OnSortParamChange -> selectSortParam(event.param)
            PokemonListScreenEvent.OnApplyFilters -> onApplyFilters()
        }
    }

    private fun getPokemons() {
        viewModelScope.launch {
            pokemonRepository.getPokemonList().cachedIn(viewModelScope).collect {
                _pokemonPagingData.value = it.map { pokemon -> pokemon.toUiModel() }
            }
        }
    }

    private fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    pokemonsList = pokemonRepository.filterPokemons(query, it.sortParam)
                        .map { pokemon ->
                            pokemon.toUiModel()
                        }
                )
            }
        }
    }

    private fun selectSortParam(param: SortParam) {
        _state.update {
            it.copy(
                sortParam = if (it.sortParam == param) SortParam.DEFAULT else param
            )
        }
    }

    private fun onApplyFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    pokemonsList = pokemonRepository.filterPokemons(it.searchQuery, it.sortParam)
                        .map { pokemon ->
                            pokemon.toUiModel()
                        },
                    isFiltersApplied = it.sortParam != SortParam.DEFAULT
                )
            }
        }
    }

}