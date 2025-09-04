package com.vsv.pokemon.presentation.pokemon_list_screen

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skydoves.landscapist.glide.GlideImage
import com.vsv.pokemon.domain.model.SortParam
import com.vsv.pokemon.domain.utils.calcDominantColor
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FilterBottomSheet(
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
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 5,
                modifier = Modifier.fillMaxWidth()
            ) {
                SortParam.entries.drop(1).forEach { param ->
                    FilterChip(
                        selected = param == state.sortParam,
                        label = {
                            Text(text = param.columnName.replaceFirstChar { it.uppercase() })
                        },
//                            colors = FilterChipDefaults.filterChipColors(
//                                selectedContainerColor = param.color,
//                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                                selectedLabelColor = getComplementaryColor(param.color)
//                            ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = true,
                            selectedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = { onEvent(PokemonListScreenEvent.OnSortParamChange(param)) }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
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

@Composable
fun PokemonItem(
    pokemon: PokemonUiModel,
    loadData: (PokemonUiModel) -> Unit
) {

    val defaultColor = CardDefaults.cardColors().containerColor
    var dominantColor by remember {
        mutableStateOf(defaultColor)
    }
    Card(
        modifier = Modifier.aspectRatio(1f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            dominantColor,
                            defaultColor,
                        ),
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(0.5f)
                ) {
                    GlideImage(
                        imageModel = { pokemon.imageUrl },
                        requestListener = {
                            object : RequestListener<Any> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Any>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Any,
                                    model: Any,
                                    target: Target<Any>?,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    calcDominantColor(resource as BitmapDrawable) { color ->
                                        dominantColor = color
                                    }
                                    return true
                                }
                            }
                        },
                    )
                }
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() }
                )
            }
        }
    }
}