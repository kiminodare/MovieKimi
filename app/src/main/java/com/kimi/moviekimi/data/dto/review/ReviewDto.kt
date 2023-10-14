package com.kimi.moviekimi.data.dto.review

data class ReviewDto(
    val id: Int,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)