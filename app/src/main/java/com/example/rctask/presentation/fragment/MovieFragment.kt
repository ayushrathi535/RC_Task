package com.example.rctask.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rctask.Utils.Resource
import com.example.rctask.adapters.MovieAdapter
import com.example.rctask.databinding.FragmentMovieBinding
import com.example.rctask.domain.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.rctask.data.remote.entity.Result


@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!


    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var myAdapter: MovieAdapter

    private var movieList: List<Result> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("fragment-->", "inside onViewCreate")
        setUpAdapter()
        setUpViewModel()


    }

    private fun setUpViewModel() {

        movieViewModel.popularMovies.observe(viewLifecycleOwner) { movieList ->
            Log.e("movies--->", movieList.size.toString())
            this.movieList = movieList
            myAdapter.differ.submitList(movieList)

        }
    }


    private fun setUpAdapter() {

        Log.e("fragment-->","inside setupadapter")
        myAdapter = MovieAdapter()
        binding.recycler.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = myAdapter
        }

        myAdapter.differ.submitList(this.movieList)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}