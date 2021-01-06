package com.example.superpupermegaproject.extensions

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide

fun ImageView.setTint(colorResourceID: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(this.resources.getColor(colorResourceID, null)));
}

fun ImageView.setImage(path: String) {
    Glide
        .with(this.context)
        .load(path)
        .into(this)
}