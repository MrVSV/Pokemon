package com.vsv.pokemon.domain.model

enum class SortParam(val columnName: String) {
    DEFAULT("default"),
    NAME("name"),
    ORDER("order"),
    HEIGHT("height"),
    WEIGHT("weight")
}