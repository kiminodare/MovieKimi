package com.kimi.moviekimi.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kimi.moviekimi.data.database.MovieDatabase
import com.kimi.moviekimi.data.entity.MovieEntity
import com.kimi.moviekimi.data.mappers.toMovieEntity
import com.kimi.moviekimi.data.network.MovieApi
import com.kimi.moviekimi.data.network.MovieRemoteMediator
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

//@RunWith(AndroidJUnit4::class)
class MovieRemoteMediatorTest {

    @Mock
    private lateinit var movieApi: MovieApi

    @Mock
    private lateinit var movieDb: MovieDatabase

    private lateinit var movieRemoteMediator: MovieRemoteMediator

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        movieRemoteMediator = MovieRemoteMediator(movieApi, movieDb, "popular", null)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `load should return success`() {
        runBlocking {
            val loadType = LoadType.REFRESH
            val state = mockk<PagingState<Int, MovieEntity>>()
            every { state.config } returns PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )
            every { state.lastItemOrNull() } returns null
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return@runBlocking
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.idMovie / PagingConfig(
                            pageSize = 20,
                            enablePlaceholders = false
                        ).pageSize) + 1
                    }
                }
            }
            val movieApi = movieApi.getMovies(
                movieList = "popular",
                apiKey = "API_KEY",
                page = loadKey,
                withGenre = null
            )
            movieDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDb.movieDao.clearAll()
                }
                val movieEntity = movieApi.results.map { it.toMovieEntity() }
                movieDb.movieDao.upsertAll(movieEntity)
            }
            Mockito.`when`(movieRemoteMediator.load(loadType, state)).thenReturn(
                RemoteMediator.MediatorResult.Success(
                    endOfPaginationReached = movieApi.results.isEmpty()
                )
            )
        }
    }
}