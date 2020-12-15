package com.example.superpupermegaproject.ui

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
import com.android.academy.fundamentals.homework.features.data.Movie
import com.example.superpupermegaproject.adapters.ActorsListAdapter
import com.example.superpupermegaproject.data.MoviesDataSource
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.extensions.setImage


class MovieDetailsFragment : Fragment() {
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            it.getLong(ARG_ID).let { _id ->
                movie = MoviesDataSource.getMovieByID(_id)
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
        val tvStoryLine = view.findViewById<TextView>(R.id.tv_storyline)
        val rvActorsList = view.findViewById<RecyclerView>(R.id.rv_actors_list)

        movie?.apply {
            ivMovieImage?.setImage(this.backdrop)

            tvAgeLimit?.setText(this.ageLimitString)
            tvTagline?.setText(this.genresString)
            rbRating?.rating = this.voteRating
            rbRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    MoviesDataSource.setRating(this.id, rating)
                }
            }
            ivLike?.setImageResource(R.drawable.unlike)
            tvReviewersCount?.setText(this.voteCountString)
            tvMovieName?.setText(this.title)
            tvStoryLine?.setText(this.overview)

            view.findViewById<TextView>(R.id.tv_cast_header)?.visibility =
                if(this.actors.isEmpty()) View.GONE else View.VISIBLE

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