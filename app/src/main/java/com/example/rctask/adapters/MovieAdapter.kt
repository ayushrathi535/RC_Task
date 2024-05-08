package com.example.rctask.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.example.rctask.R
import com.example.rctask.Utils.Constants
import com.example.rctask.data.remote.entity.Result
import com.example.rctask.databinding.MovieCardBinding

class MovieAdapter : PagingDataAdapter<Result, MovieAdapter.ViewHolder>(differCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding =MovieCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = getItem(position)
        item?.let { result->
            holder.bind(result)
        }

    }


    class ViewHolder(private val binding: MovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result?) {
            binding.title.text = item?.title
            binding.date.text = "Release Date: ${item?.releaseDate}"

            Log.e("adapter--->", item?.posterPath.toString())
            val url = item?.posterPath
//            Glide.with(binding.root).load(url).into(binding.imageView)
            binding.imageView.load(Constants.BASE_POSTER_PATH + url)
            {
//                transformations(CircleCropTransformation())
                placeholder(R.drawable.placeholder)
                crossfade(true)
            }
        }
    }


    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }

        }
    }

}

