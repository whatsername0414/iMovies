package com.example.imovies.data.repository.common.mapper

import com.example.imovies.data.model.movie.Movie
import com.example.imovies.data.repository.movie.local.entity.MovieEntity
import com.example.imovies.data.repository.movie.response.MovieResponse

class RemoteMovieMapper {
    fun mapMovieResponseToMovie(model: MovieResponse, isFavorite: Boolean): Movie {
        return Movie(
            id = model.trackId,
            title = model.trackName ?: "",
            description = model.longDescription ?: "",
            artwork = model.artworkUrl100 ?: "",
            price = model.trackPrice ?: 0.0,
            currency = model.currency ?: "USD",
            releaseYear = model.releaseDate?.split("-")?.firstOrNull() ?: "",
            genre = model.primaryGenreName ?: "",
            isFavorite = isFavorite
        )
    }
    fun mapMovieResponseToMovieEntity(model: MovieResponse): MovieEntity {
        return MovieEntity(
            trackId = model.trackId,
            trackName = model.trackName ?: "",
            longDescription = model.longDescription ?: "",
            artwork = model.artworkUrl100 ?: "",
            price = model.trackPrice ?: 0.0,
            currency = model.currency ?: "USD",
            releaseYear = model.releaseDate?.split("-")?.firstOrNull() ?: "",
            genre = model.primaryGenreName ?: ""
        )
    }

    fun mapMovieResponseToMovieEntityList(model: List<MovieResponse>): List<MovieEntity> {
        return model.map { mapMovieResponseToMovieEntity(it) }
    }


}