package com.kimi.moviekimi.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kimi.moviekimi.data.database.MovieDatabase
import com.kimi.moviekimi.data.network.MovieApi
import com.kimi.moviekimi.data.network.MovieRemoteMediator


class MovieRepository (
    private val movieApi: MovieApi,
    private val movieDb: MovieDatabase,
) {
}