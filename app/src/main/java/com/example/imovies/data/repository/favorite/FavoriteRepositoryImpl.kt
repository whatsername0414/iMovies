package com.example.imovies.data.repository.favorite

import android.util.Log
import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.HandleResponse
import com.example.imovies.data.repository.common.local.dao.FavoriteDao
import com.example.imovies.data.repository.common.mapper.LocalMovieMapper
import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.data.repository.movie.MovieRepositoryImpl
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val localMovieMapper: LocalMovieMapper,
) : FavoriteRepository, HandleResponse() {

    override suspend fun doGetFavorites(): Resource<List<Movie>> {
        val data: Resource<List<Movie>>
        try {
            val result = favoriteDao.getFavorites()
            data = success(localMovieMapper.mapFavoriteWithFlagToMovieList(result))
        } catch (e: Exception) {
            Log.d(MovieRepositoryImpl.TAG, "Error: ${e.message}")
            return exception(GENERAL_ERROR_CODE)
        }
        return data
    }

    override suspend fun doUpdateFavorite(movie: Movie): Resource<Any> {
        val data: Resource<Any>
        try {
            if (movie.isFavorite) {
                favoriteDao.deleteFavorite(movie.id)
            } else {
                val favoriteEntity = localMovieMapper.mapFromMovieToMovieEntityModel(movie)
                favoriteDao.insertFavorite(favoriteEntity)
            }
            data = success(true)
        } catch (e: Exception) {
            Log.d(MovieRepositoryImpl.TAG, "Error: ${e.message}")
            return exception(GENERAL_ERROR_CODE)
        }
        return data
    }
}