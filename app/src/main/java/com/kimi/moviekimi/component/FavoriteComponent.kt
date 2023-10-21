package com.kimi.moviekimi.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.rememberAsyncImagePainter
import com.kimi.moviekimi.R
import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.mappers.toFavoriteEntity
import com.kimi.moviekimi.navigation.MovieScreeName
import com.kimi.moviekimi.screen.MoviewListCategory
import com.kimi.moviekimi.viewModel.FavoriteMovieViewModel
import com.kimi.moviekimi.viewModel.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteListScreen(
    navController: NavController,
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    var isSearchExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val isFavorite by favoriteMovieViewModel.favoriteList.observeAsState(emptyList())
    Log.d("TAG", "MovieListScreen: $isFavorite")
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

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(isFavorite.size) { index ->
                        val movie = isFavorite[index]
                        FavoriteComponent(
                            imgUrl = movie.backdrop_path,
                            Description = movie.original_title,
                            onClick = {
                                navController.navigate(
                                    "${MovieScreeName.MovieDetailScreen.name}/${movie.idMovie}"
                                )
                            },
                            onFavoriteClick = {
                                favoriteMovieViewModel.deleteFavoriteEntityById(movie.idMovie)
                            },
                            isFavorite = true
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteComponent(
    imgUrl: String? = null,
    Description: String = "Description",
    onClick: () -> Unit = {},
    onFavoriteClick: suspend () -> Unit = {},
    isFavorite: Boolean = false,
) {
    val offset = Offset(5.0f, 10.0f)
    val webView = false
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(10.dp),
//            .clickable(onClick = onClick)
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        border = BorderStroke(1.dp, Color.Black),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    "https://image.tmdb.org/t/p/original/$imgUrl",
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .padding(10.dp),
                    text = Description,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                        shadow = Shadow(
                            color = Color.Black, offset = offset, blurRadius = 3f
                        )
                    ),
                    maxLines = 2,
                    softWrap = true,
                )
                Image(
                    colorFilter = if (isFavorite) {
                        ColorFilter.tint(Color.Yellow)
                    } else {
                        ColorFilter.tint(Color.White)
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .padding(bottom = 8.dp)
                        .clickable {
                            CoroutineScope(Dispatchers.Main).launch {
                                onFavoriteClick()
                            }
                        },
                    painter = painterResource(id = R.drawable.round_star_border_24),
                    contentDescription = "Favorite"
                )
            }
        }
    }
}