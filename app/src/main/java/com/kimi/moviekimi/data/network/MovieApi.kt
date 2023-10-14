package com.kimi.moviekimi.data.network

import com.kimi.moviekimi.data.dto.genre.Genre
import com.kimi.moviekimi.data.dto.MovieDTO
import com.kimi.moviekimi.data.dto.movieDetail.MovieDetail
import com.kimi.moviekimi.data.dto.review.ReviewDto
import com.kimi.moviekimi.data.dto.trailer.TrailerDTO
import com.kimi.moviekimi.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface MovieApi {
    @GET("movie/{movieList}")
    suspend fun getMovies(
        @Path("movieList") movieList: String,
        @Query("api_key") apiKey: String? = null,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("with_genres") withGenre: String? = null,
    ): MovieDTO

    @GET("genre/movie/list")
    suspend fun getGenreMovies(
        @Query("api_key") apiKey: String? = Constants.API_KEY,
        @Query("language") language: String? = "en",
    ): Genre

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: Int? = null,
        @Query("api_key") apiKey: String? = Constants.API_KEY,
    ) : MovieDetail

    @GET("movie/{movie_id}/videos")
    suspend fun getTrailerVideos(
        @Path ("movie_id") movieId: Int? = null,
        @Query("api_key") apiKey: String? = Constants.API_KEY,
    ) : TrailerDTO

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path ("movie_id") movieId: Int? = null,
        @Query("api_key") apiKey: String? = Constants.API_KEY,
    ) : ReviewDto
}