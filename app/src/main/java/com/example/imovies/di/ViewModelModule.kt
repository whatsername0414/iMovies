package com.example.imovies.di

import com.example.imovies.data.repository.favorite.FavoriteRepository
import com.example.imovies.data.repository.favorite.FavoriteRepositoryImpl
import com.example.imovies.data.repository.movie.MovieRepository
import com.example.imovies.data.repository.movie.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun provideMovieRepository(movieRepository: MovieRepositoryImpl): MovieRepository

    @Binds
    @ViewModelScoped
    abstract fun provideFavoriteRepository(favoriteRepository: FavoriteRepositoryImpl): FavoriteRepository
}