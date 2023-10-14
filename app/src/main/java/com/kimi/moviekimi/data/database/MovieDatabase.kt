package com.kimi.moviekimi.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kimi.moviekimi.data.dao.MovieDao
import com.kimi.moviekimi.data.entity.MovieEntity
import com.kimi.moviekimi.utils.Converters


@Database(
    entities = [MovieEntity::class],
    version = 7,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao

}