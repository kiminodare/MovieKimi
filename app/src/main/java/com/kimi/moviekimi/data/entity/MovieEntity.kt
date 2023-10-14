package com.kimi.moviekimi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val idMovie: Int,
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

val migration1to2 = object : Migration(6,7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Ganti id dari yang primary key auto generate jadi tidak ada auto generate nya
        database.execSQL("DROP TABLE `MovieEntity`")
        database.execSQL("CREATE TABLE IF NOT EXISTS `MovieEntity` (`idMovie` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id` INTEGER NOT NULL, `adult` INTEGER NOT NULL, `backdrop_path` TEXT, `genre_ids` TEXT NOT NULL, `original_language` TEXT NOT NULL, `original_title` TEXT NOT NULL, `overview` TEXT NOT NULL, `popularity` REAL NOT NULL, `poster_path` TEXT, `release_date` TEXT, `title` TEXT NOT NULL, `video` INTEGER NOT NULL, `vote_average` REAL NOT NULL, `vote_count` INTEGER NOT NULL)")
    }
}