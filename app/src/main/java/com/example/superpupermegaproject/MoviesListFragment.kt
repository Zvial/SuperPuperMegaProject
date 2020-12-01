package com.example.superpupermegaproject

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MoviesListFragment : Fragment() {
    private var onClickListener: OnClickListItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MoviesListAdapter().apply {
            this.onClickListener = this@MoviesListFragment.onClickListener
            this.updateMovieList(MoviesDataSource.moviesList)
        }

        view.findViewById<RecyclerView>(R.id.rv_movies_list)?.apply {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(view.context, 2, RecyclerView.VERTICAL, false)
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

    companion object {
        @JvmStatic
        fun newInstance() =
            MoviesListFragment()
    }

    interface OnClickListItem {
        fun onClickItem(itemID: Long)
    }
}