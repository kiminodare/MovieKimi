package com.kimi.moviekimi.dao

import com.kimi.moviekimi.data.dao.MovieDao
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MovieDaoTest {


    @Mock
    private lateinit var movieDao: MovieDao

    @Test
    fun `upsertAll should return success`() {
        val dao = movieDao.upsertAll(
            listOf(
                com.kimi.moviekimi.data.entity.MovieEntity(
                    id = 1,
                    title = "title",
                    overview = "overview",
                    poster_path = "posterPath",
                    backdrop_path = "backdropPath",
                    vote_average = 1.0,
                    vote_count = 1,
                    release_date = "releaseDate",
                    popularity = 1.0,
                    adult = false,
                    video = false,
                    original_language = "originalLanguage",
                    original_title = "originalTitle",
                    genre_ids = listOf(1, 2, 3),
                    idMovie = 1
                )
            )
        )
    }
}