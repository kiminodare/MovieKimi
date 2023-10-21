package com.kimi.moviekimi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val idMovie : Int,
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val original_language: String,
    val original_title: String,
    val overview:String,
    val popularity: Double,
    val poster_path: String? = null,
    val release_date: String? = null,
    val title: String,
    val video:Boolean,
    val vote_average: Double,
    val vote_count: Int
)