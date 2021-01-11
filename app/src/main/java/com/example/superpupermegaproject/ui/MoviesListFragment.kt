package com.example.superpupermegaproject.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superpupermegaproject.App
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.adapters.MoviesListPagingAdapter
import kotlinx.coroutines.*


class MoviesListFragment : Fragment() {
    private var onClickListener: OnClickListItem? = null
    private var rvMoviesList: RecyclerView? = null
    private var fragmentScope = CoroutineScope(Dispatchers.Main)
    private var scrollToPosition = 0
    private lateinit var viewModel: MoviesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(App.getRepositoryInstance())).get(MoviesListViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        val adapter = MoviesListPagingAdapter().apply {
            this.onClickListener = this@MoviesListFragment.onClickListener
        }

        viewModel.moviesObservable.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        rvMoviesList = view.findViewById<RecyclerView>(R.id.rv_movies_list)?.apply {
            //this.adapter = MoviesListAdapter().apply {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(view.context, 2, RecyclerView.VERTICAL, false)
        }

        scrollToPosition = savedInstanceState?.getInt(ARG_POS_NUMBER, 0) ?: 0

        return view
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(ARG_POS_NUMBER, (rvMoviesList?.layoutManager as GridLayoutManager).findFirstVisibleItemPosition())
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentScope.cancel()
    }

    companion object {
        val ARG_POS_NUMBER = "position_number"

        @JvmStatic
        fun newInstance() =
            MoviesListFragment()
    }

    interface OnClickListItem {
        fun onClickItem(itemID: Long, position: Int)
    }
}