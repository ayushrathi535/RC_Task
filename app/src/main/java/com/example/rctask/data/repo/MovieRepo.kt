package com.example.rctask.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.rctask.data.api.ApiService
import com.example.rctask.paging.MoviePagingSource
import javax.inject.Inject


class MovieRepo @Inject constructor(private val apiService: ApiService) {

    fun getPopularMovies() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { MoviePagingSource(apiService) }
    ).liveData


}