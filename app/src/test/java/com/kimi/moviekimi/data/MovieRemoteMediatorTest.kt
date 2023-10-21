package com.kimi.moviekimi.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kimi.moviekimi.data.database.MovieDatabase
import com.kimi.moviekimi.data.dto.MovieDTO
import com.kimi.moviekimi.data.dto.Result
import com.kimi.moviekimi.data.entity.MovieEntity
import com.kimi.moviekimi.data.mappers.toMovieEntity
import com.kimi.moviekimi.data.network.MovieApi
import com.kimi.moviekimi.data.network.MovieRemoteMediator
import com.kimi.moviekimi.viewModel.MovieViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
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

    @Mock
    private lateinit var MovieViewModel: MovieViewModel

    // Inisialisasi MovieRemoteMediator dengan Mockito
    private lateinit var movieRemoteMediator: MovieRemoteMediator

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        movieRemoteMediator = MovieRemoteMediator(movieApi, movieDb, "popular", null)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `load success when LoadType is REFRESH`() = runBlocking {
        val moviesPagingFlow = MovieViewModel.moviesPagingFlow




    }
}