package com.example.superpupermegaproject

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MoviesListAdapter: RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder>() {

    private val moviesList = mutableListOf<Movie>()
    var onClickListener: MoviesListFragment.OnClickListItem? = null

    fun updateMovieList(list: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListAdapter.MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MoviesListAdapter.MovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
        onClickListener?.let {listener ->
            holder.itemView.setOnClickListener { listener.onClickItem(moviesList[position].id) }
        }
    }

    override fun getItemCount() = moviesList.size

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var isLiked = false

        private val ivMovieImage        = itemView.findViewById<ImageView>(R.id.iv_movie_image)
        private val tvAgeLimit          = itemView.findViewById<TextView>(R.id.tv_age_limit)
        private val ivLike              = itemView.findViewById<ImageView>(R.id.iv_like)
        private val tvTagline           = itemView.findViewById<TextView>(R.id.tv_tagline)
        private val rbRating            = itemView.findViewById<RatingBar>(R.id.rb_rating)
        private val tvReviewersCount    = itemView.findViewById<TextView>(R.id.tv_reviewers_count)
        private val tvMovieName         = itemView.findViewById<TextView>(R.id.tv_movie_name)
        private val tvLengthOfMovie     = itemView.findViewById<TextView>(R.id.tv_length_of_movie)
        private val vLikedClickedArea   = itemView.findViewById<View>(R.id.v_like_clicked_area)

        fun bind(movie: Movie) {
            isLiked = movie.isLiked

            ivMovieImage?.let {
                Glide.with(itemView.context)
                .load(movie.imageID)
                .into(it)
            }


            tvAgeLimit?.setText(movie.ageLimitString)
            tvTagline?.setText(movie.tagsString)

            rbRating?.rating = movie.rating
            rbRating?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    movie?.let {
                        MoviesDataSource.setRating(it.id, rating)
                    }
                }
            }

            ivLike?.setImageResource(R.drawable.unlike)
            manageLikedView()
            vLikedClickedArea.setOnClickListener {
                isLiked = !isLiked
                MoviesDataSource.setLiked(movie.id, isLiked)
                manageLikedView()
            }

            tvReviewersCount?.setText(movie.reviewersCountString)
            tvMovieName?.setText(movie.name)
            tvLengthOfMovie?.setText(movie.lengthOfMovieString)
        }

        private fun manageLikedView() {
            val colorResourceID =
            if(isLiked) {
                R.color.liked_tint_color
            } else {
                R.color.cast_text_color
            }

            ivLike?.setTint(colorResourceID)
        }
    }
}