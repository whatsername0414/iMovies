package com.example.imovies.data.repository.common.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imovies.data.repository.common.local.dao.FavoriteDao
import com.example.imovies.data.repository.common.local.dao.MovieDao
import com.example.imovies.data.repository.favorite.local.entity.FavoriteEntity
import com.example.imovies.data.repository.movie.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
}