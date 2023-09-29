package com.example.imovies.data.repository.common.mapper

import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.favorite.local.entity.FavoriteEntity
import com.example.imovies.data.repository.favorite.local.entity.FavoriteWithFlag
import com.example.imovies.data.repository.movie.local.entity.MovieWithFavorite

class LocalMovieMapper {
    fun mapMovieEntityToMovie(model: MovieWithFavorite): Movie {
        return Movie(
            id = model.movieEntity.trackId,
            title = model.movieEntity.trackName,
            artwork = model.movieEntity.artwork,
            description = model.movieEntity.longDescription,
            price = model.movieEntity.price,
            currency = model.movieEntity.currency,
            genre = model.movieEntity.genre,
            releaseYear = model.movieEntity.releaseYear,
            isFavorite = model.isFavorite()
        )
    }

    fun mapMovieWithFavoriteToMovieList(model: List<MovieWithFavorite>): List<Movie> {
        return model.map { mapMovieEntityToMovie(it) }
    }

    fun mapFavoriteWithFlagToMovie(model: FavoriteWithFlag): Movie {
        return Movie(
            id = model.trackId,
            title = model.trackName,
            description = model.longDescription,
            artwork = model.artwork,
            price = model.price,
            currency = model.currency,
            genre = model.genre,
            releaseYear = model.releaseYear,
            isFavorite = model.isFavorite()
        )
    }

    fun mapFavoriteWithFlagToMovieList(model: List<FavoriteWithFlag>): List<Movie> {
        return model.map { mapFavoriteWithFlagToMovie(it) }
    }

    fun mapFromMovieToMovieEntityModel(model: Movie): FavoriteEntity {
        return FavoriteEntity(
            trackId = model.id,
            trackName = model.title,
            longDescription = model.description,
            artwork = model.artwork,
            price = model.price,
            currency = model.currency,
            genre = model.genre,
            releaseYear = model.releaseYear
        )
    }


}