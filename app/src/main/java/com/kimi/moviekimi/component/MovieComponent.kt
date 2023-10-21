package com.kimi.moviekimi.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.kimi.moviekimi.R
import com.kimi.moviekimi.data.dto.review.Result
import com.kimi.moviekimi.navigation.MovieScreeName
import com.kimi.moviekimi.viewModel.MovieViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.Dispatcher


@Preview(showBackground = true)
@Composable
fun MovieItem(
    imgUrl: String? = null,
    Description: String = "Description",
    onClick: () -> Unit = {},
    onFavoriteClick: suspend  () -> Unit = {},
    isFavorite: Boolean = false,
) {
    val offset = Offset(5.0f, 10.0f)
    val webView = false
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(10.dp)
//            .clickable(onClick = onClick)
            ,
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
                            }  },
                    painter = painterResource(id = R.drawable.round_star_border_24),
                    contentDescription = "Favorite"
                )
            }
        }
    }
}

@Composable
fun MovieDetail(
    navController: NavController,
    keyYoutube: String? = null,
    bannerUrl: String? = null,
    posterUrl: String? = null,
    title: String? = "Movie Title",
    description: String? = stringResource(R.string.description),
    rating: String? = "7.5",
    review: List<Result>? = null,
    onClick: () -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isPopupVisible by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Box(
                modifier = Modifier.wrapContentHeight()
            ) {
                Image(
                    painter = if (bannerUrl != null) {
                        rememberAsyncImagePainter(
                            "https://image.tmdb.org/t/p/original/$bannerUrl",
                        )
                    } else {
                        painterResource(R.drawable.bannerblank)
                    },
                    contentDescription = "Banner"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .height(200.dp)
                        .offset(y = 100.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .size(200.dp),
                        painter = if (posterUrl != null) {
                            rememberAsyncImagePainter(
                                "https://image.tmdb.org/t/p/original/$posterUrl",
                            )
                        } else {
                            painterResource(R.drawable.posterblank)
                        },
                        contentDescription = "Poster"
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .offset(y = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = title ?: "Movie Title",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF),
                            ),
                            maxLines = 2,
                            softWrap = true,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp)
                    .offset(y = 10.dp)
                    .clickable { isExpanded = !isExpanded },
                text = description ?: stringResource(R.string.description),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF),
                ),
                maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                softWrap = true,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            colorFilter = ColorFilter.tint(Color.Yellow),
                            painter = painterResource(R.drawable.baseline_star_rate_24),
                            contentDescription = "Rating"
                        )
                        Text(
                            text = rating ?: "7.5",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF),
                            ),
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color.Gray
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(onClick = {
//                    isPopupVisible = true
                    onClick()
                }) {
                    Text(text = "Watch Trailer")
                }
            }
            LazyColumn(content =
            {
                items(review?.size ?: 0) { index ->
                    review?.get(index)?.let { result ->
                        ReviewItem(
                            imagePath = result.author_details.avatar_path,
                            reviewContent = result.content,
                            name = result.author_details.name ?: "Name",
                            username = result.author_details.username ?: "Username",
                            rating = result.author_details.rating.toString() ?: "10/10",
                        )
                    }
                }
            }
            )
        }
    if (isPopupVisible) {
        PopUpYoutube(
            onDismiss = { isPopupVisible = false },
            videoId = keyYoutube ?: "",
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        )
    }
}

@Composable
fun ReviewItem(
    imagePath: String? = null,
    reviewContent: String = stringResource(R.string.loremipsum),
    name: String = "Name",
    username: String = "Username",
    rating: String = "10/10",
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F1F),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                painter = if (imagePath != null) {
                    rememberAsyncImagePainter(
                        "https://image.tmdb.org/t/p/original/$imagePath",
                    )
                } else {
                    painterResource(R.drawable.round_person_24)
                },
                contentDescription = "Banner"
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                    ),
                )
                Text(
                    text = username,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                    ),
                )
                Text(
                    text = rating,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                    ),
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Text(
                text = reviewContent,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF),
                ),
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                softWrap = true,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun PopUpYoutube(
    onDismiss: () -> Unit = {},
    videoId: String = "null",
    modifier: Modifier = Modifier,
) {
    var player: YouTubePlayer? by remember { mutableStateOf(null) }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        content = {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                factory = {
                    var view = YouTubePlayerView(it)
                    view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            player = youTubePlayer
                            videoId.let { videoId ->
                                player?.loadVideo(videoId!!, 0f)
                            }
                        }
                    })
                    view
                })
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun seachMovieItemPreview(
    onDismiss: () -> Unit = {},
    mainMovieViewModel: MovieViewModel = hiltViewModel(),
    navController: NavController,
) {
    val searchText = remember { mutableStateOf("") }
    val isDone = remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
    ) {
        Column {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                    isDone.value = false
                },
                label = {
                    Text(text = "Search Movie")
                },
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(R.drawable.round_search_24),
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    Image(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(R.drawable.round_close_24),
                        contentDescription = "Close"
                    )
                },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        isDone.value = true
                    }
                ),
            )
            if (isDone.value) {
                val MainModelResultSearch = mainMovieViewModel.getSearchMovie(searchText.value)
                val DataResultSearch = mainMovieViewModel.DataSearchMovie.value
                if (DataResultSearch.data?.results?.size == 0) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        text = "No Result",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFFFFF),
                        ),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .background(Color(0xFF40484D)),
                        contentPadding = PaddingValues(5.dp),
                        content =
                    {
                        items(DataResultSearch.data?.results?.size ?: 0) { index ->
                            DataResultSearch.data?.results?.get(index)?.let { result ->
                                SearchItem(
                                    id = index,
                                    Name = result.name,
                                    onClick = {
                                        navController.navigate("${MovieScreeName.MovieDetailScreen.name}/${result.id}")
                                    }
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(10.dp)
                                        .background(
                                            Color(0xFF000000)
                                        ))
                            }
                        }
                    }
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun SearchItem(
    id: Int = 0,
    Name: String = "Name",
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = Name,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
            ),
        )
    }
}