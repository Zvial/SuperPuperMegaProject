package com.example.superpupermegaproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MovieDetailsFragment : Fragment() {
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            it.getLong(ARG_ID)?.let { _id ->
                movie = MoviesDataSource.moviesList.first { _movie ->
                    _movie.id == _id
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvBack = view.findViewById<TextView>(R.id.tv_back)
        tvBack?.setOnClickListener {
            activity?.onBackPressed()
        }

        val ivMovieImage = view.findViewById<ImageView>(R.id.iv_movie_image)
        val tvAgeLimit = view.findViewById<TextView>(R.id.tv_age_limit)
        val ivLike = view.findViewById<ImageView>(R.id.iv_like)
        val tvTagline = view.findViewById<TextView>(R.id.tv_tagline)
        val rbRating = view.findViewById<RatingBar>(R.id.rb_rating)
        val tvReviewersCount = view.findViewById<TextView>(R.id.tv_reviewers_count)
        val tvMovieName = view.findViewById<TextView>(R.id.tv_movie_name)
        val tvLengthOfMovie = view.findViewById<TextView>(R.id.tv_length_of_movie)
        val rvActorsList = view.findViewById<RecyclerView>(R.id.rv_actors_list)

        movie?.apply {
            ivMovieImage?.let {
                Glide.with(view.context)
                    .load(this.detailImageID)
                    .into(it)
                // it.setTint(R.color.cast_text_color)
            }

            tvAgeLimit?.setText(this.ageLimitString)
            tvTagline?.setText(this.tagsString)
            rbRating?.rating = this.rating.toFloat()
            rbRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    MoviesDataSource.setRating(this.id, rating)
                }
            }
            ivLike?.setImageResource(R.drawable.unlike)
            tvReviewersCount?.setText(this.reviewersCountString)
            tvMovieName?.setText(this.name)
            tvLengthOfMovie?.setText(this.lengthOfMovieString)

            val actorsAdapter = ActorsListAdapter()
            actorsAdapter.submitList(this.actors)

            rvActorsList?.let {
                it.adapter = actorsAdapter
                it.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    companion object {
        const val ARG_ID = "movie_id"

        @JvmStatic
        fun newInstance(id: Long) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ID, id)
                }
            }
    }
}