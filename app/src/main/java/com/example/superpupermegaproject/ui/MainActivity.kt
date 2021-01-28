package com.example.superpupermegaproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.superpupermegaproject.App
import com.example.superpupermegaproject.R

class MainActivity : AppCompatActivity(), MoviesListFragment.OnClickListItem {
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

    override fun onStart() {
        super.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        activityIsDestroying = true
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onClickItem(itemID: Long, position: Int) {
        setFragment(MovieDetailsFragment.newInstance(itemID), true)
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