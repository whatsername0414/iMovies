package com.example.imovies.data.repository.favorite

import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.common.resource.Resource

interface FavoriteRepository {

    suspend fun doGetFavorites(): Resource<List<Movie>>

    suspend fun doUpdateFavorite(movie: Movie): Resource<Any>
}