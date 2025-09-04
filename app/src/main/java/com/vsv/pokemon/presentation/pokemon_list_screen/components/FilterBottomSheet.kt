package com.vsv.pokemon.presentation.pokemon_list_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.presentation.pokemon_list_screen.PokemonListScreenEvent
import com.vsv.pokemon.presentation.pokemon_list_screen.PokemonListScreenState
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    state: PokemonListScreenState,
    onEvent: (PokemonListScreenEvent) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
        modifier = Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sort by".uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    maxItemsInEachRow = 5,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SortParam.entries.drop(1).forEach { param ->
                        FilterChip(
                            selected = param == state.sortParam,
                            label = {
                                Text(text = param.columnName.replaceFirstChar { it.uppercase() })
                            },
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = true,
                                selectedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            onClick = { onEvent(PokemonListScreenEvent.OnSortParamChange(param)) }
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Filter by type".uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    maxItemsInEachRow = 5,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    state.pokemonTypes.forEach { type ->
                        FilterChip(
                            selected = type.isSelected,
                            label = {
                                Text(text = type.name.replaceFirstChar { it.uppercase() })
                            },
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = true,
                                selectedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            onClick = { onEvent(PokemonListScreenEvent.OnPokemonTypeSelected(type)) }
                        )
                    }
                }
            }
            Button(
                onClick = {
                    onEvent(PokemonListScreenEvent.OnApplyFilters)
                    scope.launch {
                        bottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) onDismissRequest()
                    }
                },
                modifier = Modifier
            ) {
                Text(
                    text = "Apply Filters"
                )
            }
        }
    }
}