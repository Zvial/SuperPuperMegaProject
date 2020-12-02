package com.example.superpupermegaproject.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superpupermegaproject.model.MoviesDataSource
import com.example.superpupermegaproject.adapters.MoviesListAdapter
import com.example.superpupermegaproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MoviesListFragment : Fragment() {
    private var onClickListener: OnClickListItem? = null
    private var rvMoviesList: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMoviesList = view.findViewById<RecyclerView>(R.id.rv_movies_list)?.apply {
            this.adapter = MoviesListAdapter().apply {
                this.onClickListener = this@MoviesListFragment.onClickListener
                //this.updateMovieList(MoviesDataSource.moviesList)
            }
            this.layoutManager = GridLayoutManager(view.context, 2, RecyclerView.VERTICAL, false)
        }
        updateRecyclerViewList()

        view.findViewById<FloatingActionButton>(R.id.fab_mix_list)?.apply {
            setOnClickListener {
                MoviesDataSource.shuffleList()
                updateRecyclerViewList()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnClickListItem) {
            onClickListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()

        onClickListener = null
    }

    private fun updateRecyclerViewList() {
        rvMoviesList?.let {_rvMoviesList ->
            with(_rvMoviesList.adapter as MoviesListAdapter) {
                val duCallback = MoviesListAdapter.MoviesDiffUtilCallback(this.moviesList, MoviesDataSource.moviesList)
                val diff = DiffUtil.calculateDiff(duCallback, true)
                diff.dispatchUpdatesTo(this)
                this.updateMovieList(MoviesDataSource.moviesList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MoviesListFragment()
    }

    interface OnClickListItem {
        fun onClickItem(itemID: Long)
    }
}