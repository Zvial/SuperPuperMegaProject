package com.example.superpupermegaproject

import androidx.recyclerview.widget.DiffUtil

class ActorsDiffUtilCallback : DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean =
        oldItem.name == newItem.name


    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean =
        oldItem == newItem

}