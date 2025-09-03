package com.vsv.pokemon.domain.model.errors

enum class RemoteError : Error {
    SERVER,
    TIMEOUT,
    NO_INTERNET,
    SERIALIZATION,
    NOT_FOUND,
    UNKNOWN
}
