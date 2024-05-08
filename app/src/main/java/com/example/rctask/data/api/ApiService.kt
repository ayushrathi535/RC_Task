package com.example.rctask.data.api

import com.example.rctask.Utils.Constants
import com.example.rctask.data.remote.entity.PopularMovie
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("movie/popular?")
    suspend fun getPopularMovies(
        @Query ("page") page:Int,
        @Query("api_key") api_key:String = Constants.API_KEY
    ): PopularMovie

}