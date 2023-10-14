package com.kimi.moviekimi.data.model

data class DataOrException<T, Booelan, E: Exception>(
    var data: T? = null,
    var isLoading: Booelan? = null,
    var error: E? = null
)
