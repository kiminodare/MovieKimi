package com.kimi.moviekimi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.entity.FavoriteEntity


@Dao
interface FavoriteDao {

    @Insert
    fun insertAll(news: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM FavoriteEntity")
    fun getAll(): List<FavoriteEntity>
}