package com.kimi.moviekimi.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.kimi.moviekimi.data.database.FavoriteDatabase
import com.kimi.moviekimi.data.database.MovieDatabase
import com.kimi.moviekimi.data.entity.MovieEntity
import com.kimi.moviekimi.data.entity.migration1to2
import com.kimi.moviekimi.data.network.MovieApi
import com.kimi.moviekimi.data.network.MovieRemoteMediator
import com.kimi.moviekimi.repository.GenreRepository
import com.kimi.moviekimi.repository.MovieDetailRepository
import com.kimi.moviekimi.repository.ReviewRepository
import com.kimi.moviekimi.repository.SearchMovieRepository
import com.kimi.moviekimi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_db"
        )
            .addMigrations(migration1to2)
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "favorite_db"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDatabase: FavoriteDatabase) = favoriteDatabase.favoriteDao

    @Provides
    @Singleton
    fun provideGenreRepository(api: MovieApi) = GenreRepository(api)

    @Provides
    @Singleton
    fun provideMovieDetailRepository(api: MovieApi) = MovieDetailRepository(api)

    @Provides
    @Singleton
    fun provideReviewRepository(api: MovieApi) = ReviewRepository(api)

    @Provides
    @Singleton
    fun provideSearchMovieRepository(api: MovieApi) = SearchMovieRepository(api)
}