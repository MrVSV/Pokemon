package com.vsv.pokemon.presentation.pokemon_list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.vsv.pokemon.presentation.pokemon_list_screen.components.FilterBottomSheet
import com.vsv.pokemon.presentation.pokemon_list_screen.components.PokemonItem
import com.vsv.pokemon.presentation.ui_model.PokemonUiModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokemonListScreenRoot(
) {
    val viewModel: PokemonListScreenViewModel = koinViewModel()

    PokemonListScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        pokemons = viewModel.pokemonPagingData.collectAsLazyPagingItems(),
        onEvent = viewModel::onEvent,
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    state: PokemonListScreenState,
    pokemons: LazyPagingItems<PokemonUiModel>,
    onEvent: (PokemonListScreenEvent) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val lazyGridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val showScrollToTopButton by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex > 20
        }
    }
    var showBottomSheet by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        onEvent(PokemonListScreenEvent.GetPokemons)
    }
    LaunchedEffect(pokemons.loadState.refresh) {
        if (pokemons.loadState.refresh is LoadState.Error) {
            pullToRefreshState.animateToHidden()
        }
    }
    BackHandler(state.searchQuery.isNotEmpty() || state.isFiltersApplied) {
        onEvent(PokemonListScreenEvent.ClearFilters)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = state.searchQuery,
                onValueChange = { onEvent(PokemonListScreenEvent.OnSearchValueChange(it)) },
                label = {
                    Text(
                        text = "Search"
                    )
                },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { showBottomSheet = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
        }
        PullToRefreshBox(
            isRefreshing = pokemons.loadState.refresh is LoadState.Loading,
            onRefresh = { onEvent(PokemonListScreenEvent.GetPokemons) },
            state = pullToRefreshState
        ) {
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 12.dp),
                state = lazyGridState,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.searchQuery.isEmpty() && !state.isFiltersApplied) {
                    items(pokemons.itemCount) { index ->
                        pokemons[index]?.let { pokemon ->
                            PokemonItem(pokemon) { }
                        }
                    }
                } else {
                    items(state.pokemonsList) { pokemon ->
                        PokemonItem(pokemon) { }
                    }
                }
                if (pokemons.loadState.append is LoadState.Loading) {
                    item(
                        span = { GridItemSpan(2) }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth(0.2f)

                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            if (showScrollToTopButton) {
                IconButton(
                    onClick = { scope.launch { lazyGridState.scrollToItem(0) } },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Magenta
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 20.dp, end = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null
                    )
                }
            }
        }
    }
    if (showBottomSheet) {
        FilterBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            state = state,
            onEvent = onEvent,
        )
    }
}

