package com.example.superpupermegaproject

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView


class MoviesListFragment : Fragment() {
    private var onSelectListener: OnSelectMovieItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list_cardview, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardView = view.findViewById<MaterialCardView>(R.id.cardview)
        cardView?.setOnClickListener {
            onSelectListener?.onSelect(1)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSelectMovieItem) {
            onSelectListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()

        onSelectListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MoviesListFragment()
    }

    interface OnSelectMovieItem {
        fun onSelect(id: Int)
    }
}