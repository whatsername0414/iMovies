package com.example.imovies.data.repository.common.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imovies.data.repository.favorite.local.entity.FavoriteEntity
import com.example.imovies.data.repository.favorite.local.entity.FavoriteWithFlag

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("SELECT *, 1 AS isFavorite FROM favorites")
    suspend fun getFavorites(): List<FavoriteWithFlag>

    @Query("SELECT * FROM FAVORITES WHERE trackId = :id")
    suspend fun getFavorite(id: Int): FavoriteEntity?

    @Query("DELETE FROM FAVORITES WHERE trackId = :id")
    suspend fun deleteFavorite(id: Int)
}