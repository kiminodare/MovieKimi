package com.kimi.moviekimi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kimi.moviekimi.data.dao.FavoriteDao
import com.kimi.moviekimi.data.entity.FavoriteEntity
import com.kimi.moviekimi.utils.Converters


@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract val favoriteDao: FavoriteDao
}