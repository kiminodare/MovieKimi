package com.kimi.moviekimi.navigation

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.paging.compose.collectAsLazyPagingItems
import com.kimi.moviekimi.component.FavoriteComponent
import com.kimi.moviekimi.component.FavoriteListScreen
import com.kimi.moviekimi.data.database.FavoriteDatabase
import com.kimi.moviekimi.screen.MovieDetailScreen
import com.kimi.moviekimi.screen.MovieListScreen
import com.kimi.moviekimi.viewModel.FavoriteMovieViewModel
import com.kimi.moviekimi.viewModel.MovieViewModel


@ExperimentalComposeUiApi
@Composable
fun MovieNavigation() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<MovieViewModel>()
    val movie = viewModel.moviesPagingFlow.collectAsLazyPagingItems()

    NavHost(
        navController = navController,
        startDestination = MovieScreeName.MovieScreen.name
    ) {
        composable(
            MovieScreeName.MovieScreen.name
        ) {
            val favoriteViewModel = hiltViewModel<FavoriteMovieViewModel>()
            MovieListScreen(
                navController = navController,
                movie = movie,
                favoriteMovieViewModel = favoriteViewModel
            )
        }
        composable(
            "${MovieScreeName.MovieDetailScreen.name}/{movieId}"
        ) {
            MovieDetailScreen(
                movieId = it.arguments?.getString("movieId")?.toInt() ?: 0,
                navController = navController
            )
        }
        composable(
            MovieScreeName.FavoriteMovieScreen.name
        ) {
            val favoriteViewModel = hiltViewModel<FavoriteMovieViewModel>()
            FavoriteListScreen(
                navController = navController,
                favoriteMovieViewModel = favoriteViewModel
            )
        }
        composable(
            route = "youtube/{videoId}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://www.youtube.com/watch?v={videoId}"
                }
            )
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            val context = LocalContext.current

            if (videoId != null) {
                val youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
                context.startActivity(youtubeIntent)
            } else {
                Toast.makeText(context, "Video tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}