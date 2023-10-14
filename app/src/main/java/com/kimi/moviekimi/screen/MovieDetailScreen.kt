package com.kimi.moviekimi.screen

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kimi.moviekimi.component.MovieDetail
import com.kimi.moviekimi.repository.MovieDetailRepository
import com.kimi.moviekimi.viewModel.MovieViewModel


@Composable
fun MovieDetailScreen(
    movieId : Int,
    navController: NavController,
) {
    val viewModel = hiltViewModel<MovieViewModel>()
    val movieDetail = viewModel.getMovieDetail(movieId)
    val trailer = viewModel.getTrailerVideos(movieId).data?.results?.get(0)?.key ?: ""
    val review = viewModel.getReview(movieId).data?.results
    MovieDetail(
        bannerUrl = movieDetail.data?.backdrop_path,
        posterUrl = movieDetail.data?.poster_path,
        title = movieDetail.data?.original_title,
        description = movieDetail.data?.overview,
        rating = movieDetail.data?.vote_average.toString(),
        keyYoutube = trailer,
        review = review,
        navController = navController
    )
}