package com.example.superpupermegaproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.data.Movie
import com.example.superpupermegaproject.extensions.setTint
import com.example.superpupermegaproject.ui.MoviesListFragment

class MoviesListAdapter: RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder>() {

    val moviesList = mutableListOf<Movie>()
    var onClickListener: MoviesListFragment.OnClickListItem? = null

    fun updateMovieList(list: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
        onClickListener?.let {listener ->
            holder.itemView.setOnClickListener { listener.onClickItem(moviesList[position].id, position) }
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

    class MoviesDiffUtilCallback(
        private val oldList: List<Movie>,
        private val newList: List<Movie>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }
}