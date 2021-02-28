package com.zavierdev.cinemacatalogue.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.constant.CinemaType
import com.zavierdev.cinemacatalogue.data.model.SearchCinemaModel
import com.zavierdev.cinemacatalogue.databinding.ItemSearchCinemaBinding
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity
import com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow.DetailTvShowActivity

class SearchCinemaRecyclerViewAdapter :
    RecyclerView.Adapter<SearchCinemaRecyclerViewAdapter.ViewHolder>() {
    var searchData = ArrayList<SearchCinemaModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder =
            ItemSearchCinemaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchData[position])
    }

    override fun getItemCount(): Int = searchData.size

    inner class ViewHolder(private val binder: ItemSearchCinemaBinding) :
        RecyclerView.ViewHolder(binder.root) {
        fun bind(cinema: SearchCinemaModel) {
            with(binder) {
                tvTitle.text = cinema.title
                tvReleaseDate.text = cinema.release
                tvDescription.text = cinema.overview

                Glide.with(itemView)
                    .load(cinema.poster)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.bg_lightgray)
                            .error(R.drawable.bg_lightgray)
                    )
                    .into(imgPoster)
            }

            itemView.setOnClickListener {
                when (cinema.type) {
                    CinemaType.MOVIE -> {
                        val intent = Intent(itemView.context, DetailMovieActivity::class.java)
                        intent.putExtra(DetailMovieActivity.EXTRA_ID, cinema.id)
                        itemView.context.startActivity(intent)
                    }
                    CinemaType.TVSHOW -> {
                        val intent = Intent(itemView.context, DetailTvShowActivity::class.java)
                        intent.putExtra(DetailTvShowActivity.EXTRA_ID, cinema.id)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }
}