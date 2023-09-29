package com.example.imovies.data.repository.movie.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val longDescription: String,
    val artwork: String,
    val price: Double,
    val currency: String,
    val genre: String,
    val releaseYear: String,
)

data class MovieWithFavorite(
    @Embedded
    val movieEntity: MovieEntity,
    @ColumnInfo(name = "isFavorite")
    val isFavoriteFlag: Int
) {
    fun isFavorite(): Boolean {
        return isFavoriteFlag != 0
    }
}
