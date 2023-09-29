package com.example.imovies.data.repository.movie

import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.common.resource.Resource
import com.example.imovies.data.repository.movie.local.entity.MovieEntity

interface MovieRepository {

    suspend fun doGetMovie(id: Int): Resource<Movie>?

    suspend fun doGetMovies(term: String, isForceUpdate: Boolean): Resource<List<Movie>>?

    suspend fun insertMovies(movieEntities: List<MovieEntity>)

    suspend fun getMoviesLocal(term: String): List<Movie>

}