package com.vsv.pokemon.presentation.pokemon_list_screen.components

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skydoves.landscapist.glide.GlideImage
import com.vsv.pokemon.domain.utils.calcDominantColor
import com.vsv.pokemon.presentation.ui_model.PokemonUiModel

@Composable
fun PokemonItem(
    pokemon: PokemonUiModel,
    onClick: (PokemonUiModel) -> Unit
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