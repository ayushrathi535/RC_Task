package com.example.rctask.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rctask.data.repo.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.rctask.data.remote.entity.Result


@HiltViewModel
class MovieViewModel @Inject constructor( private val repo: MovieRepo) : ViewModel() {


    private val _popularMovies = MutableLiveData<List<Result>>()
    val popularMovies: LiveData<List<Result>>
        get() = _popularMovies


    val moviesList = repo.getPopularMovies().cachedIn(viewModelScope)

//    fun getPopularMovies() {
//        viewModelScope.launch {
//            _popularMovies.value = Resource.Loading(null)
//            try {
//                val movies = repo.getPopularMovies()
//                _popularMovies.value = Resource.Success(movies.body()!!.results)
//            } catch (e: Exception) {
//                _popularMovies.value = Resource.Error("something wrong",data = null)
//            }
//        }
//    }

}