package com.zavierdev.cinemacatalogue.ui.home.favorite.movie

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity

class FavoriteMovieRecyclerViewAdapter(private val callback: MoveActivityCallback) :
    PagedListAdapter<MovieEntity, FavoriteMovieRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                Log.d("OLD MOVIE", oldItem.title)
                Log.d("NEW MOVIE", newItem.title)
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_cinema, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)

        Glide.with(holder.itemView)
            .load(model?.poster)
            .apply(
                RequestOptions.placeholderOf(R.drawable.bg_lightgray)
                    .error(R.drawable.bg_lightgray)
            )
            .into(holder.imgPoster)

        holder.tvTitle.text = model?.title
        holder.tvReleaseDate.text = model?.release
        holder.tvDescription.text = model?.overview

        holder.itemView.setOnClickListener {
            model?.let { callback.onClick(it) }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.img_poster)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvReleaseDate: TextView = itemView.findViewById(R.id.tv_release_date)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
    }

    interface MoveActivityCallback {
        fun onClick(model: MovieEntity)
    }
}