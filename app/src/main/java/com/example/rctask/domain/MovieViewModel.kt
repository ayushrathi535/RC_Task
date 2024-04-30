package com.example.rctask.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rctask.Utils.Resource
import com.example.rctask.data.repo.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.rctask.data.remote.entity.Result


@HiltViewModel
class MovieViewModel @Inject constructor(private val repo: MovieRepo) : ViewModel() {


    private val _popularMovies = MutableLiveData<List<Result>>()
    val popularMovies: LiveData<List<Result>>
        get() = _popularMovies

    init {
        getPopularMovies()
    }
    fun getPopularMovies() {
        viewModelScope.launch {
            repo.getPopularMovies().let { response ->
                if (response.isSuccessful) {
                    _popularMovies.postValue(response.body()?.results)
                } else {
                    Log.e("error-->", "${response.code()}")
                }

            }
        }
    }

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