package com.example.rctask.data.repo

import com.example.rctask.Utils.Resource
import com.example.rctask.data.api.ApiService
import com.example.rctask.data.remote.entity.PopularMovie
import retrofit2.Response
import javax.inject.Inject


class MovieRepo @Inject constructor(private val apiService: ApiService) {

    suspend fun getPopularMovies()= apiService.getPopularMovies()

}