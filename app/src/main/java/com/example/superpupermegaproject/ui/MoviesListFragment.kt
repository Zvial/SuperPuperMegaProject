package com.example.superpupermegaproject.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superpupermegaproject.App
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.adapters.MoviesListPagingAdapter
import com.example.superpupermegaproject.model.MovieResultState
import com.example.superpupermegaproject.ui.viewmodels.MoviesListViewModel
import com.example.superpupermegaproject.ui.viewmodels.ViewModelFactory
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*


class MoviesListFragment : Fragment() {
    private var onClickListener: OnClickListItem? = null
    private var rvMoviesList: RecyclerView? = null
    private var tvStateText: TextView? = null
    private var btnUpdateFirst: MaterialButton? = null
    private var tvStateNext: TextView? = null
    private var btnUpdateNext: MaterialButton? = null
    private var etSearchQuery: EditText? = null

    //private lateinit var adapter: MoviesListPagingAdapter
    private var fragmentScope = CoroutineScope(Dispatchers.Main)
    private var scrollToPosition = 0
    private lateinit var viewModel: MoviesListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(App.getMoviesInteractorInstance())).get(
                MoviesListViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        initViews(view)

        viewModel.moviesObservable.observe(viewLifecycleOwner) {
            (rvMoviesList?.adapter as MoviesListPagingAdapter).submitList(it)
        }

        scrollToPosition = savedInstanceState?.getInt(ARG_POS_NUMBER, 0) ?: 0

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateObservable.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieResultState.Loading -> {
                    setViewWhenLoadingState()
                }
                is MovieResultState.LoadingNextPage -> {
                    //setViewWhenLoadingPageState()
                }
                is MovieResultState.Sucess -> {
                    setViewByWorkedState()
                }
                is MovieResultState.Error -> {
                    setViewWhenLoadingErrorState()
                }
                is MovieResultState.ErrorNextPage -> {
                    //setViewWhenLoadingPageErrorState()
                }
                else -> {
                    setViewByWorkedState()
                }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(ARG_POS_NUMBER, (rvMoviesList?.layoutManager as GridLayoutManager).findFirstVisibleItemPosition())
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentScope.cancel()
    }

    private fun initViews(parent: View) {
        prepareMoviesList(parent)
        tvStateText = parent.findViewById(R.id.tv_state)
        btnUpdateFirst = parent.findViewById(R.id.btn_update_first)
        btnUpdateFirst?.setOnClickListener {
            updateMoviesList()
        }

        tvStateNext = parent.findViewById(R.id.tv_state_next)
        btnUpdateNext = parent.findViewById(R.id.btn_update_next)
        btnUpdateNext?.setOnClickListener {
            updateMoviesList()
        }

        etSearchQuery = parent.findViewById(R.id.et_search)
        etSearchQuery?.doAfterTextChanged {
            //viewModel.searchTextChanged(it.toString())
            fragmentScope.launch {
                viewModel.queryChannel.value = it.toString()
            }

        }
    }

    private fun updateMoviesList() {
        val lastItemInList = with(rvMoviesList?.layoutManager as GridLayoutManager) {
            this.findLastVisibleItemPosition()
        }

        fragmentScope.launch {
            viewModel.updateMovies()

            if (lastItemInList >= 0) {
                with(rvMoviesList?.layoutManager as GridLayoutManager) {
                    this.scrollToPosition(lastItemInList)
                }
            }
        }
    }

    private fun prepareMoviesList(parent: View) {
        val adapter = MoviesListPagingAdapter().apply {
            this.onClickListener = this@MoviesListFragment.onClickListener
        }

        rvMoviesList = parent.findViewById<RecyclerView>(R.id.rv_movies_list)?.apply {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(parent.context, 2, RecyclerView.VERTICAL, false)
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if(checkShownLastItemsInList()) {
                        when(viewModel.stateObservable.value) {
                            is MovieResultState.LoadingNextPage -> {
                                setViewWhenLoadingPageState()
                            }
                            is MovieResultState.ErrorNextPage -> {
                                setViewWhenLoadingPageErrorState()
                            }
                            else -> setViewByWorkedState()
                        }
                    } else {
                        setViewByWorkedState()
                    }
                }
            })
        }
    }

    private fun setViewWhenLoadingState() {
        tvStateText?.text = "Загрузка"
        tvStateText?.visibility = View.VISIBLE
        rvMoviesList?.visibility = View.GONE
        btnUpdateFirst?.visibility = View.GONE
        tvStateNext?.visibility = View.GONE
        btnUpdateNext?.visibility = View.GONE
    }

    private fun setViewByWorkedState() {
        rvMoviesList?.visibility = View.VISIBLE
        tvStateText?.visibility = View.GONE
        btnUpdateFirst?.visibility = View.GONE
        tvStateNext?.visibility = View.GONE
        btnUpdateNext?.visibility = View.GONE
    }

    private fun setViewWhenLoadingErrorState() {
        tvStateText?.text = "Уууупс... Ошибка загрузки данных"
        tvStateText?.visibility = View.VISIBLE
        btnUpdateFirst?.visibility = View.VISIBLE
        rvMoviesList?.visibility = View.GONE
        tvStateNext?.visibility = View.GONE
        btnUpdateNext?.visibility = View.GONE
    }

    private fun setViewWhenLoadingPageState() {
        tvStateNext?.text = "Загрузка..."
        tvStateNext?.visibility = View.VISIBLE
        rvMoviesList?.visibility = View.VISIBLE
        tvStateText?.visibility = View.GONE
        btnUpdateFirst?.visibility = View.GONE
        btnUpdateNext?.visibility = View.GONE
    }

    private fun setViewWhenLoadingPageErrorState() {
        tvStateNext?.text = "Уууупс... Ошибка загрузки данных"
        tvStateNext?.visibility = View.VISIBLE
        btnUpdateNext?.visibility = View.VISIBLE
        rvMoviesList?.visibility = View.VISIBLE
        tvStateText?.visibility = View.GONE
        btnUpdateFirst?.visibility = View.GONE
    }

    private fun checkShownLastItemsInList(): Boolean =
        with(rvMoviesList?.layoutManager as GridLayoutManager) {
            itemCount - findLastCompletelyVisibleItemPosition() <= 2
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