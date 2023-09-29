package com.example.imovies.data.repository.favorite.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
class FavoriteEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val longDescription: String,
    val artwork: String,
    val price: Double,
    val currency: String,
    val releaseYear: String,
    val genre: String,
)

class FavoriteWithFlag(
    val trackId: Int,
    val trackName: String,
    val longDescription: String,
    val artwork: String,
    val price: Double,
    val currency: String,
    val genre: String,
    val releaseYear: String,
    @ColumnInfo(name = "isFavorite")
    val isFavoriteFlag: Int
) {
    fun isFavorite(): Boolean {
        return this.isFavoriteFlag != 0
    }
}