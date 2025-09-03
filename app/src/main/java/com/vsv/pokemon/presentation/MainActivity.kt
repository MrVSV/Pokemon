package com.vsv.pokemon.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vsv.pokemon.domain.repository.PokemonRepository
import com.vsv.pokemon.presentation.pokemon_list_screen.PokemonListScreenRoot
import com.vsv.pokemon.presentation.theme.PokemonTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val api: PokemonRepository by inject()
            val scope = rememberCoroutineScope()
            PokemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        PokemonListScreenRoot {  }
//                        Button(
//                            onClick = {
//                                scope.launch(Dispatchers.IO) {
//                                    api.getPokemonList()
//                                }
//                            }
//                        ) { }
                    }
                }
            }
        }
    }
}
