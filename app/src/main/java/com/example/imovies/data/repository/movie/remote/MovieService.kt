package com.example.imovies.data.repository.movie.remote

import com.example.imovies.data.repository.common.response.BaseResponse
import com.example.imovies.data.repository.movie.response.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("search")
    suspend fun doGetMovies(
        @Query("country") country: String = "au",
        @Query("media") media: String = "movie",
        @Query("term") term: String
    ): Response<BaseResponse<List<MovieResponse>>>

    @GET("lookup")
    suspend fun doGetMovie(
        @Query("id") id: Int
    ): Response<BaseResponse<List<MovieResponse>>>

}