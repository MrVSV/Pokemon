package com.vsv.pokemon.domain.model

enum class SortParam(val columnName: String) {
    DEFAULT("default"),
    NAME("name"),
    ORDER("order"),
    HEIGHT("height"),
    WEIGHT("weight"),
    HP("hp"),
    ATTACK("attack"),
    DEFENSE("defense"),
    SPECIAL_ATTACK("special_attack"),
    SPECIAL_DEFENSE("special_defense"),
    SPEED("speed"),
}