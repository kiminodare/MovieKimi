package com.kimi.moviekimi.repository

import android.util.Log
import com.kimi.moviekimi.data.dto.movieDetail.MovieDetail
import com.kimi.moviekimi.data.dto.trailer.TrailerDTO
import com.kimi.moviekimi.data.model.DataOrException
import com.kimi.moviekimi.data.network.MovieApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(
    private val movieDetailApi: MovieApi
) {
    // MovieDetail
    val DataMovieDetail = DataOrException<MovieDetail, Boolean, Exception>()

    suspend fun getMovieDetail(id: Int): DataOrException<MovieDetail, Boolean, Exception> {
        try {
            Log.d("MovieDetailRepository", "getMovieDetail: $id")
            DataMovieDetail.isLoading = true
            DataMovieDetail.data = movieDetailApi.getDetailMovie(
                movieId = id
            )
            if (DataMovieDetail.data.toString().isNotEmpty()) DataMovieDetail.isLoading = false
        } catch (exception: Exception) {
            DataMovieDetail.error = exception
        }
        return DataMovieDetail
    }

    // Trailer
    val DataTrailer = DataOrException<TrailerDTO, Boolean, Exception>()

    suspend fun getTrailerVideos(id: Int): DataOrException<TrailerDTO, Boolean, Exception> {
        try {
            Log.d("MovieDetailRepository", "getTrailerVideos: $id")
            DataTrailer.isLoading = true
            DataTrailer.data = movieDetailApi.getTrailerVideos(
                movieId = id
            )
            if (DataTrailer.data.toString().isNotEmpty()) DataTrailer.isLoading = false
        } catch (exception: Exception) {
            DataTrailer.error = exception
        }
        return DataTrailer
    }
}