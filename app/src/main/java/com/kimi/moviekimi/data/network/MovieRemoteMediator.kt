package com.kimi.moviekimi.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kimi.moviekimi.data.database.MovieDatabase
import com.kimi.moviekimi.data.entity.MovieEntity
import com.kimi.moviekimi.data.mappers.toMovieEntity
import com.kimi.moviekimi.utils.Constants
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieApi: MovieApi,
    private val movieDb: MovieDatabase,
    private val category: String,
    private val withGenre: String?
) : RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                        (lastItem.idMovie / state.config.pageSize) + 1
                    }
                }
            }

            val movieApi = movieApi.getMovies(
                movieList = category,
                apiKey = Constants.API_KEY,
                page = loadKey,
                withGenre = withGenre
            )

            movieDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDb.movieDao.clearAll()
                }
                val movieEntity = movieApi.results.map { it.toMovieEntity() }
                movieDb.movieDao.upsertAll(movieEntity)
            }

            MediatorResult.Success(
                endOfPaginationReached = movieApi.results.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}