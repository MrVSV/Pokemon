package com.vsv.pokemon.presentation.pokemon_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vsv.pokemon.domain.mappers.toUiModel
import com.vsv.pokemon.domain.model.LoadingStatus
import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.domain.model.onError
import com.vsv.pokemon.domain.model.onSuccess
import com.vsv.pokemon.domain.repository.PokemonRepository
import com.vsv.pokemon.presentation.ui_model.PokemonTypeUiModel
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
            PokemonListScreenEvent.GetPokemons -> getPokemonTypes()
            is PokemonListScreenEvent.OnSearchValueChange -> onSearchQueryChange(event.query)
            is PokemonListScreenEvent.OnSortParamChange -> selectSortParam(event.param)
            is PokemonListScreenEvent.OnPokemonTypeSelected -> selectPokemonType(event.type)
            PokemonListScreenEvent.OnApplyFilters -> onApplyFilters()
        }
    }

    private fun getPokemonTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(loadingStatus = LoadingStatus.LOADING) }
            pokemonRepository.getPokemonTypes()
                .onSuccess { types ->
                    _state.update {
                        it.copy(
                            pokemonTypes = types.map { type -> type.toUiModel() },
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                    getPokemons()
                }
                .onError {
                    _state.update { it.copy(loadingStatus = LoadingStatus.ERROR) }
                }
        }
    }

    private fun getPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepository.getPokemonList().cachedIn(viewModelScope).collect {
                _pokemonPagingData.value = it.map { pokemon -> pokemon.toUiModel() }
            }
        }
    }

    private fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        onApplyFilters()
    }

    private fun selectSortParam(param: SortParam) {
        _state.update {
            it.copy(
                sortParam = if (it.sortParam == param) SortParam.DEFAULT else param
            )
        }
    }

    private fun selectPokemonType(type: PokemonTypeUiModel) {
        _state.update {
            it.copy(
                pokemonTypes = it.pokemonTypes.map { pokemonType ->
                    if (pokemonType.name == type.name) {
                        pokemonType.copy(isSelected = !pokemonType.isSelected)
                    } else {
                        pokemonType
                    }
                }
            )
        }
    }

    private fun onApplyFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    pokemonsList = pokemonRepository.filterPokemons(
                        searchQuery = it.searchQuery,
                        sortParam = it.sortParam,
                        types = if (it.pokemonTypes.all { type -> !type.isSelected }) {
                            it.pokemonTypes
                                .map { type -> type.name }
                        } else {
                            it.pokemonTypes.filter { type -> type.isSelected }
                                .map { type -> type.name }
                        }
                    ).map { pokemon ->
                        pokemon.toUiModel()
                    },
                    isFiltersApplied = it.pokemonTypes.any { type -> type.isSelected } || it.sortParam != SortParam.DEFAULT
                )
            }
        }
    }
}