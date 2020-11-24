package com.example.superpupermegaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), MoviesListFragment.OnSelectMovieItem {
    private val fragmentContainerID = R.id.fragment_frame
    private var activityIsDestroying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setFragment(MoviesListFragment.newInstance(), false)
        }
    }

    override fun onRestart() {
        super.onRestart()

        activityIsDestroying = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        activityIsDestroying = true
    }

    override fun onSelect(id: Int) {
        setFragment(MovieDetailsFragment.newInstance(id), true)
    }

    private fun setFragment(fragment: Fragment, addToBackStack: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            replace(fragmentContainerID, fragment)

            if (addToBackStack) {
                addToBackStack(null)
            }

            if (!activityIsDestroying) { //проверка, чтобы не словить крэш после onSaveInstanceState
                commit()
            }
        }
    }
}