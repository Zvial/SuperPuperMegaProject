package com.example.superpupermegaproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.superpupermegaproject.R
import com.example.superpupermegaproject.data.Actor

class ActorsListAdapter :
    ListAdapter<Actor, ActorsListAdapter.ActorsViewHolder>(ActorsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        return ActorsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_holder_actor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ActorsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivActorImage = itemView.findViewById<ImageView>(R.id.iv_actor_image)
        private val tvActorName = itemView.findViewById<TextView>(R.id.tv_actor_name)

        fun bind(actor: Actor) {
            ivActorImage?.load(actor.picture) {
                error(R.drawable.ic_download_error)
                placeholder(R.drawable.ic_download_progress)
                fallback(R.drawable.ic_download_error)
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }

            tvActorName.setText(actor.name)
        }
    }

    class ActorsDiffUtilCallback : DiffUtil.ItemCallback<Actor>() {
        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean =
            oldItem.name == newItem.name


        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean =
            oldItem == newItem

    }
}