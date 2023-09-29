package com.example.imovies.data.repository.movie

import android.util.Log
import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.HandleResponse
import com.example.imovies.data.repository.common.local.dao.FavoriteDao
import com.example.imovies.data.repository.common.local.dao.MovieDao
import com.example.imovies.data.repository.common.mapper.LocalMovieMapper
import com.example.imovies.data.repository.common.mapper.RemoteMovieMapper
import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.data.repository.movie.local.entity.MovieEntity
import com.example.imovies.data.repository.movie.remote.MovieService
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val favoriteDao: FavoriteDao,
    private val movieService: MovieService,
    private val remoteMovieMapper: RemoteMovieMapper,
    private val localMovieMapper: LocalMovieMapper,
) : MovieRepository, HandleResponse() {
    override suspend fun doGetMovie(id: Int): Resource<Movie>? {
        var data: Resource<Movie>? = null
        try {
            val result = movieService.doGetMovie(id = id)
            result.body()?.results?.firstOrNull()?.let {
                val isFavorite = favoriteDao.getFavorite(id = id) != null
                val movie = remoteMovieMapper.mapMovieResponseToMovie(it, isFavorite)
                data = success(movie)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error: ${e.message}")
            return exception(GENERAL_ERROR_CODE)
        }
        return data
    }

    override suspend fun doGetMovies(term: String, isForceUpdate: Boolean): Resource<List<Movie>>? {
        var data: Resource<List<Movie>>? = null
        try {
            if (isForceUpdate) {
                val result = movieService.doGetMovies(term = term)
                result.body()?.results?.let {
                    val movieEntities = remoteMovieMapper.mapMovieResponseToMovieEntityList(it)
                    insertMovies(movieEntities)
                    data = success(getMoviesLocal(""))
                }
            } else {
                data = success(getMoviesLocal(term = term))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error: ${e.message}")
            return exception(GENERAL_ERROR_CODE)
        }
        return data
    }

    override suspend fun insertMovies(movieEntities: List<MovieEntity>) {
        movieDao.insertMovies(movieEntities)
    }

    override suspend fun getMoviesLocal(term: String): List<Movie> {
        val result = movieDao.getMovies(term = term)
        return localMovieMapper.mapMovieWithFavoriteToMovieList(result)
    }

    companion object {
        const val TAG = "MovieRepositoryImpl"
    }
}