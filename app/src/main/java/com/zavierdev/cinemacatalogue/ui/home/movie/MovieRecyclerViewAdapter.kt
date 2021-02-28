package com.zavierdev.cinemacatalogue.ui.home.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.data.model.DiscoverCinemaModel

class MovieRecyclerViewAdapter() :
    RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>() {
    var movies: ArrayList<DiscoverCinemaModel> = ArrayList()
    private lateinit var onClickItem: OnClickItem;

    fun setOnClickItem(onClickItem: OnClickItem) {
        this.onClickItem = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cinema_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = movies[position]

        holder.tvTitle.text = model.title
        holder.tvDuration.text = model.release

        Glide.with(holder.itemView)
            .load(model.poster)
            .apply(
                RequestOptions.placeholderOf(R.drawable.bg_lightgray)
                    .error(R.drawable.bg_lightgray)
            )
            .into(holder.imgPoster)

        holder.itemView.setOnClickListener {
            this.onClickItem.onClick(model)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnClickItem {
        fun onClick(movie: DiscoverCinemaModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.img_poster)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_release_date)
    }
}