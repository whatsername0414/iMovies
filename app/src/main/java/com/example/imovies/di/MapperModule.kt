package com.example.imovies.di

import com.example.imovies.data.repository.common.mapper.LocalMovieMapper
import com.example.imovies.data.repository.common.mapper.RemoteMovieMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {


    @Singleton
    @Provides
    fun provideRemoteMovieMapper(): RemoteMovieMapper {
        return RemoteMovieMapper()
    }

    @Singleton
    @Provides
    fun provideLocalMovieMapper(): LocalMovieMapper {
        return LocalMovieMapper()
    }

}