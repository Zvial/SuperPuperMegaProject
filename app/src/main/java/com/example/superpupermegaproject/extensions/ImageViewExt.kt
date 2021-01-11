package com.example.superpupermegaproject.extensions

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat

fun ImageView.setTint(colorResourceID: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(this.resources.getColor(colorResourceID, null)));
}
