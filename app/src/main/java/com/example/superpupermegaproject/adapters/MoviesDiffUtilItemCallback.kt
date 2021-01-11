package com.example.superpupermegaproject.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.superpupermegaproject.data.Movie

class MoviesDiffUtilItemCallback() : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}