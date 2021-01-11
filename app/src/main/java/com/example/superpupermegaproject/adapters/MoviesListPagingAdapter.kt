package com.example.superpupermegaproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.extensions.setTint
import com.example.superpupermegaproject.ui.MoviesListFragment

class MoviesListPagingAdapter: PagedListAdapter<Movie, MovieViewHolder>(MoviesDiffUtilItemCallback()) {

    var onClickListener: MoviesListFragment.OnClickListItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
            onClickListener?.let { listener ->
                holder.itemView.setOnClickListener { listener.onClickItem(movie.id, position) }
            }
        }
    }

}