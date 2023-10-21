package com.kimi.moviekimi.repository

import com.kimi.moviekimi.data.dto.searchMovie.SearchMovieDTO
import com.kimi.moviekimi.data.model.DataOrException
import com.kimi.moviekimi.data.network.MovieApi
import javax.inject.Inject


class SearchMovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    val DataSearchMovie = DataOrException<SearchMovieDTO, Boolean, Exception>()

    suspend fun getSearchMovie(keyword : String): DataOrException<SearchMovieDTO, Boolean, Exception> {
        try {
            DataSearchMovie.isLoading = true
            DataSearchMovie.data = movieApi.searchMovie(
                query = keyword
            )
            if (DataSearchMovie.data.toString().isNotEmpty()) DataSearchMovie.isLoading = false
        } catch (exception: Exception) {
            DataSearchMovie.error = exception
        }
        return DataSearchMovie
    }

}