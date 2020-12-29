package com.example.superpupermegaproject.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.academy.fundamentals.homework.features.data.Movie
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.adapters.ActorsListAdapter
import com.example.superpupermegaproject.data.MoviesDataSource
import com.example.superpupermegaproject.extensions.setImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MovieDetailsFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this, ViewModelFactory(context?.applicationContext as Application)).get(MovieDetailsViewModel::class.java) }
    private var fragmentScope = CoroutineScope(Dispatchers.Main)
    private var tvBack: TextView? = null
    private var ivMovieImage: ImageView? = null
    private var tvAgeLimit: TextView? = null
    private var ivLike: ImageView? = null
    private var tvTagline: TextView? = null
    private var rbRating: RatingBar? = null
    private var tvReviewersCount: TextView? = null
    private var tvMovieName: TextView? = null
    private var tvStoryLine: TextView? = null
    private var tvCastHeader: TextView? = null
    private var rvActorsList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            it.getLong(ARG_ID).let { _id ->
                fragmentScope.launch {
                    viewModel.getMovie(_id)
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

        initViews(view)

        viewModel.movieObservable.observe(viewLifecycleOwner) { movie ->
            bind(movie)
        }

        return view
    }

    private fun initViews(view: View) {
        tvBack = view.findViewById<TextView>(R.id.tv_back)
        tvBack?.setOnClickListener {
            activity?.onBackPressed()
        }

        ivMovieImage = view.findViewById<ImageView>(R.id.iv_movie_image)
        tvAgeLimit = view.findViewById<TextView>(R.id.tv_age_limit)
        ivLike = view.findViewById<ImageView>(R.id.iv_like)
        tvTagline = view.findViewById<TextView>(R.id.tv_tagline)
        rbRating = view.findViewById<RatingBar>(R.id.rb_rating)
        tvReviewersCount = view.findViewById<TextView>(R.id.tv_reviewers_count)
        tvMovieName = view.findViewById<TextView>(R.id.tv_movie_name)
        tvStoryLine = view.findViewById<TextView>(R.id.tv_storyline)
        tvCastHeader = view.findViewById<TextView>(R.id.tv_cast_header)
        rvActorsList = view.findViewById<RecyclerView>(R.id.rv_actors_list)
    }

    private fun bind(movie: Movie) {
        ivMovieImage?.setImage(movie.backdrop)

        tvAgeLimit?.setText(movie.ageLimitString)
        tvTagline?.setText(movie.genresString)
        rbRating?.rating = movie.voteRating

        ivLike?.setImageResource(R.drawable.unlike)
        tvReviewersCount?.setText(movie.voteCountString)
        tvMovieName?.setText(movie.title)
        tvStoryLine?.setText(movie.overview)

        tvCastHeader?.visibility =
            if(movie.actors.isEmpty()) View.GONE else View.VISIBLE

        val actorsAdapter = ActorsListAdapter()
        actorsAdapter.submitList(movie.actors)

        rvActorsList?.let {
            it.adapter = actorsAdapter
            it.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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