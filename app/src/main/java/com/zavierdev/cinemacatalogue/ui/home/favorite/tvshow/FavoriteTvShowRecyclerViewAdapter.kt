package com.zavierdev.cinemacatalogue.ui.home.favorite.tvshow

import android.content.Intent
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
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity
import com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow.DetailTvShowActivity

class FavoriteTvShowRecyclerViewAdapter :
    PagedListAdapter<TvShowEntity, FavoriteTvShowRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
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
            val holderContext = holder.itemView.context
            val intent = Intent(holderContext, DetailTvShowActivity::class.java)
            intent.putExtra(DetailTvShowActivity.EXTRA_ID, model?.id)
            holderContext.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.img_poster)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvReleaseDate: TextView = itemView.findViewById(R.id.tv_release_date)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
    }
}