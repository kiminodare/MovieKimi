package com.kimi.moviekimi.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.entity.MovieEntity

@Dao
interface MovieDao {
    @Upsert
    fun upsertAll(news: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM MovieEntity")
    fun clearAll()
}