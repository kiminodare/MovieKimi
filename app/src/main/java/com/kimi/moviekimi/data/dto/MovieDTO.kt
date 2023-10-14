package com.kimi.moviekimi.data.dto

data class MovieDTO(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)