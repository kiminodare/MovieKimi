package com.kimi.moviekimi.repository

import com.kimi.moviekimi.data.dto.genre.Genre
import com.kimi.moviekimi.data.model.DataOrException
import com.kimi.moviekimi.data.network.MovieApi
import javax.inject.Inject


class GenreRepository @Inject constructor(
    private val genreApi: MovieApi
) {

    val DataGenre = DataOrException<Genre, Boolean, Exception>()


    suspend fun getAllGenre() : DataOrException<Genre, Boolean, Exception> {
        try {
            DataGenre.isLoading = true
            DataGenre.data = genreApi.getGenreMovies()
            if (DataGenre.data.toString().isNotEmpty()) DataGenre.isLoading = false
        } catch (exception: Exception) {
            DataGenre.error = exception
        }
        return DataGenre
    }
}