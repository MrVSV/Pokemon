package com.vsv.pokemon.presentation.ui_model

import androidx.compose.ui.graphics.Color

data class PokemonUiModel(
    val name: String,
    val imageUrl: String,
    val color: PokemonColor?,
    val loadingStatus: LoadingStatus
)

enum class PokemonColor(val colorName: String, val color: Color) {
    BLACK("black", Color(0xFF000000)),
    BLUE("blue", Color(0xFF0000FF)),
    BROWN("brown", Color(0xFF8B4513)),
    GRAY("gray", Color(0xFF808080)),
    GREEN("green", Color(0xFF008000)),
    PINK("pink", Color(0xFFFFC0CB)),
    PURPLE("purple", Color(0xFF800080)),
    RED("red", Color(0xFFFF0000)),
    WHITE("white", Color(0xFFFFFFFF)),
    YELLOW("yellow", Color(0xFFFFFF00)),
    UNKNOWN("UNKNOWN", Color(0xFF5B5858));

    companion object {
        fun fromString(nameColor: String): PokemonColor? {
            return PokemonColor.entries.find { value ->
                value.colorName.equals(nameColor, ignoreCase = true)
            }
        }
    }
}
