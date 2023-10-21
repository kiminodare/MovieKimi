package com.kimi.moviekimi.data.mappers

import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.entity.FavoriteEntity

fun Result.toFavoriteEntity() : FavoriteEntity {
    return FavoriteEntity(
        idMovie = 0,
        id = id,
        adult = adult,
        backdrop_path = backdrop_path,
        genre_ids = genre_ids,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count
    )
}

fun FavoriteEntity.toResult() : Result {
    return Result(
        adult = adult,
        backdrop_path = backdrop_path,
        genre_ids = genre_ids,
        id = id,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count
    )
}