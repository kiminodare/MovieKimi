package com.kimi.moviekimi.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kimi.moviekimi.data.database.MovieDatabase
import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.dto.genre.Genre
import com.kimi.moviekimi.data.dto.movieDetail.MovieDetail
import com.kimi.moviekimi.data.dto.review.ReviewDto
import com.kimi.moviekimi.data.dto.searchMovie.SearchMovieDTO
import com.kimi.moviekimi.data.dto.trailer.TrailerDTO
import com.kimi.moviekimi.data.mappers.toResult
import com.kimi.moviekimi.data.model.DataOrException
import com.kimi.moviekimi.data.network.MovieApi
import com.kimi.moviekimi.data.network.MovieRemoteMediator
import com.kimi.moviekimi.repository.GenreRepository
import com.kimi.moviekimi.repository.MovieDetailRepository
import com.kimi.moviekimi.repository.MovieRepository
import com.kimi.moviekimi.repository.ReviewRepository
import com.kimi.moviekimi.repository.SearchMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDb: MovieDatabase,
    private val genreRepository: GenreRepository,
    private val movieDetailRepository: MovieDetailRepository,
    private val reviewRepository: ReviewRepository,
    private val searchMovieRepository: SearchMovieRepository
) : ViewModel() {

    // Movies Paging
    private val query = MutableStateFlow("popular")
    fun setQuery(query: String) {
        this.query.value = query
    }

    val queryFlow: StateFlow<String> = query

    private val genreOption = MutableStateFlow("")
    fun setGenreOption(genreOption: String) {
        this.genreOption.value = genreOption
    }

    val genreOptionFlow: StateFlow<String> = genreOption

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    val moviesPagingFlow = combine(
        queryFlow,
        genreOptionFlow
    ) { query, genreOption ->
        Pair(query, genreOption)
    }.flatMapLatest { (query, genreOption) ->
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            remoteMediator = MovieRemoteMediator(
                movieApi = movieApi,
                movieDb = movieDb,
                category = query,
                withGenre = genreOption
            ),
            pagingSourceFactory = { movieDb.movieDao.pagingSource() }
        ).flow
    }.map { pagingData ->
        pagingData.map { it.toResult() }
    }.cachedIn(viewModelScope)



    //Genre
    val DataGenre: MutableState<DataOrException<Genre, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    init {
        getAllGenre()
    }

    private fun getAllGenre() {
        viewModelScope.launch {
            DataGenre.value.isLoading = true
            DataGenre.value = genreRepository.getAllGenre()
            if (DataGenre.value.data.toString().isNotEmpty()) {
                DataGenre.value.isLoading = false
            }
        }
    }

    //Movie Detail
    val DataMovieDetail: MutableState<DataOrException<MovieDetail, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    fun getMovieDetail(movieId: Int): DataOrException<MovieDetail, Boolean, Exception> {
        viewModelScope.launch {
            DataMovieDetail.value.isLoading = true
            DataMovieDetail.value = movieDetailRepository.getMovieDetail(movieId)
            if (DataMovieDetail.value.data.toString().isNotEmpty()) {
                DataMovieDetail.value.isLoading = false
            }
        }
        return DataMovieDetail.value
    }

    //Trailer
    val DataTrailer: MutableState<DataOrException<TrailerDTO, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    fun getTrailerVideos(movieId: Int): DataOrException<TrailerDTO, Boolean, Exception> {
        viewModelScope.launch {
            DataTrailer.value.isLoading = true
            DataTrailer.value = movieDetailRepository.getTrailerVideos(movieId)
            if (DataTrailer.value.data.toString().isNotEmpty()) {
                DataTrailer.value.isLoading = false
            }
        }
        return DataTrailer.value
    }

    //Review
    val DataReview: MutableState<DataOrException<ReviewDto, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    fun getReview(movieId: Int): DataOrException<ReviewDto, Boolean, Exception> {
        viewModelScope.launch {
            DataReview.value.isLoading = true
            DataReview.value = reviewRepository.getReview(
                id = movieId
            )
            if (DataReview.value.data.toString().isNotEmpty()) {
                DataReview.value.isLoading = false
            }
        }
        return DataReview.value
    }

    val DataSearchMovie: MutableState<DataOrException<SearchMovieDTO, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    fun getSearchMovie(keyword: String) {
        viewModelScope.launch {
            DataSearchMovie.value.isLoading = true // Set isLoading to true first
            val result = searchMovieRepository.getSearchMovie(keyword)
            if (result.data.toString().isNotEmpty()) {
                DataSearchMovie.value = result // Update the value after you have the data
            }
            DataSearchMovie.value.isLoading = false // Set isLoading to false
            Log.d("TAG", "getSearchMovie Hasil: ${DataSearchMovie.value.isLoading}")
        }
    }
}