package com.kimi.moviekimi.repository

import com.kimi.moviekimi.data.dto.review.ReviewDto
import com.kimi.moviekimi.data.model.DataOrException
import com.kimi.moviekimi.data.network.MovieApi
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    val DataReview = DataOrException<ReviewDto, Boolean, Exception>()

    suspend fun getReview(id: Int): DataOrException<ReviewDto, Boolean, Exception> {
        try {
            DataReview.isLoading = true
            DataReview.data = movieApi.getReviews(
                movieId = id
            )
            if (DataReview.data.toString().isNotEmpty()) DataReview.isLoading = false
        } catch (exception: Exception) {
            DataReview.error = exception
        }
        return DataReview
    }
}