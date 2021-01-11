package com.example.superpupermegaproject.adapters

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.extensions.setTint

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

        //ivMovieImage?.setImage(movie.backdrop)
        ivMovieImage?.load(movie.poster) {
            crossfade(true)
            placeholder(R.drawable.ic_download_progress)
            error(R.drawable.ic_download_error)
        }

        tvAgeLimit?.setText(movie.ageLimitString)
        tvTagline?.setText(movie.genresString)

        rbRating?.rating = movie.voteRating
        rbRating?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                movie.let {
                    //TODO
                    //MoviesDataSource.setRating(it.id, rating)
                }
            }
        }

        ivLike?.setImageResource(R.drawable.unlike)
        manageLikedView()
        vLikedClickedArea.setOnClickListener {
            isLiked = !isLiked
            //MoviesDataSource.setLiked(movie.id, isLiked)
            manageLikedView()
        }

        tvReviewersCount?.setText(movie.voteCountString)
        tvMovieName?.setText(movie.title)
        tvLengthOfMovie?.setText(movie.runtimeString)
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