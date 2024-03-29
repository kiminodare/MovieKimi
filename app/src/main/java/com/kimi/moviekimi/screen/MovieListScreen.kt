package com.kimi.moviekimi.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kimi.moviekimi.R
import com.kimi.moviekimi.component.MovieItem
import com.kimi.moviekimi.component.seachMovieItemPreview
import com.kimi.moviekimi.data.database.FavoriteDatabase
import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.mappers.toFavoriteEntity
import com.kimi.moviekimi.navigation.MovieScreeName
import com.kimi.moviekimi.viewModel.FavoriteMovieViewModel
import com.kimi.moviekimi.viewModel.MovieViewModel
import okhttp3.internal.wait


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    navController: NavController,
    movie: LazyPagingItems<Result>,
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    var isSearchExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val isFavorite by favoriteMovieViewModel.favoriteList.observeAsState(emptyList())
    Log.d("TAG", "MovieListScreen: $isFavorite")
    LaunchedEffect(key1 = movie.loadState) {
        if (movie.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (movie.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    if (movie.loadState.refresh is LoadState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .padding(8.dp),
                ) {
                    if (isSearchExpanded) {
                        seachMovieItemPreview(
                            onDismiss = { isSearchExpanded = false },
                            navController = navController
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clickable(onClick = {
                                isSearchExpanded = !isSearchExpanded
                            }),
                        painter = painterResource(R.drawable.round_search_24),
                        contentDescription = "Search"
                    )
                    Column {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "Movie"
                        )
                        MoviewListCategory(
                            viewModel = hiltViewModel<MovieViewModel>(),
                            navController = navController
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LaunchedEffect(key1 = movie.loadState) {
                    if (movie.loadState.refresh is LoadState.Error) {
                        Toast.makeText(
                            context,
                            "Error: " + (movie.loadState.refresh as LoadState.Error).error.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (movie.loadState.refresh is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(movie.itemCount) { index ->
                                movie[index]?.let { result ->
                                    val isFavorite = isFavorite.any { it.id == result.id }
                                    Log.d("TAG", "MovieListScreen: $isFavorite")
                                    MovieItem(
                                        imgUrl = result.backdrop_path,
                                        Description = result.title,
                                        onClick = {
                                            navController.navigate("${MovieScreeName.MovieDetailScreen.name}/${result.id}")
                                        },
                                        onFavoriteClick = {
                                            if (isFavorite) {
                                                favoriteMovieViewModel.deleteFavoriteEntityById(
                                                    result.id
                                                )
                                            } else {
                                                favoriteMovieViewModel.insertFavoriteEntity(result.toFavoriteEntity())
                                            }
                                        },
                                        isFavorite = isFavorite
                                    )
                                }
                            }
                            item {
                                if (movie.loadState.append is LoadState.Loading) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun MoviewListCategory(
    viewModel: MovieViewModel,
    navController: NavController
) {
    val movie = viewModel.moviesPagingFlow.collectAsLazyPagingItems()
    val moviee: LazyPagingItems<Result> = movie
    val selectedGenre = viewModel.genreOptionFlow.collectAsState()

    if (viewModel.DataGenre.value.isLoading == false) {
        Row {
            MoviewListCategoryItem(
                category = "Favorite",
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        navController.navigate(MovieScreeName.FavoriteMovieScreen.name)
                    }
            )
            LazyRow(content = {
                items(viewModel.DataGenre.value.data?.genres?.size ?: 0) { index ->
                    val genre = viewModel.DataGenre.value.data?.genres?.get(index)
                    val isGenreSelected = selectedGenre.value == genre?.id.toString()
                    MoviewListCategoryItem(
                        category = genre?.name,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                if (navController.currentBackStackEntry?.destination?.route != MovieScreeName.MovieScreen.name) {
                                    navController.navigate(MovieScreeName.MovieScreen.name)
                                }
                                viewModel.setGenreOption(genre?.id.toString())
                                movie.refresh()
                            }
                    )
                }
            })
        }
    } else {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun MoviewListCategoryItem(
    category: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        border = BorderStroke(1.dp, Color.Black),
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(10.dp),
            text = category ?: "Category",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
            ),
            maxLines = 2,
            softWrap = true,
        )
    }
}