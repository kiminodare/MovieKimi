package com.kimi.moviekimi.repository

import com.kimi.moviekimi.data.dao.FavoriteDao
import com.kimi.moviekimi.data.entity.FavoriteEntity
import javax.inject.Inject


class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {



    fun getAllFavoriteEntities(): List<FavoriteEntity> {
        return favoriteDao.getAll()
    }

    fun insertFavoriteEntity(favoriteEntity: FavoriteEntity) {
        favoriteDao.insertAll(favoriteEntity)
    }

    fun deleteFavoriteEntityById(id: Int) {
        favoriteDao.deleteById(id)
    }
}