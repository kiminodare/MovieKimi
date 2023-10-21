package com.kimi.moviekimi.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.dto.genre.Genre
import com.kimi.moviekimi.data.entity.FavoriteEntity
import com.kimi.moviekimi.data.model.DataOrException
import com.kimi.moviekimi.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val favoriteMovieRepository: FavoriteRepository
) : ViewModel() {

    val favoriteList: LiveData<List<FavoriteEntity>> = liveData {
        emit(favoriteMovieRepository.getAllFavoriteEntities())
    }

    suspend fun insertFavoriteEntity(favoriteEntity: FavoriteEntity) {
        withContext(Dispatchers.IO) {
            favoriteMovieRepository.insertFavoriteEntity(favoriteEntity)
        }
    }

    fun deleteFavoriteEntityById(id: Int) {
        favoriteMovieRepository.deleteFavoriteEntityById(id)
    }
}