package com.zavierdev.cinemacatalogue.ui.detailcinema

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.constant.CinemaType
import com.zavierdev.cinemacatalogue.data.model.SimilarCinemaModel
import com.zavierdev.cinemacatalogue.databinding.ItemSimilarCinemaBinding
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity
import com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow.DetailTvShowActivity

class SimilarRecyclerViewAdapter(private val cinemaType: CinemaType) :
    RecyclerView.Adapter<SimilarRecyclerViewAdapter.ViewHolder>() {
    var similarCinemas: ArrayList<SimilarCinemaModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder =
            ItemSimilarCinemaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(similarCinemas[position])
    }

    override fun getItemCount(): Int = similarCinemas.size

    inner class ViewHolder(private val binding: ItemSimilarCinemaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cinema: SimilarCinemaModel) {
            with(binding) {
                Glide.with(itemView)
                    .load(cinema.poster)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.bg_lightgray)
                            .error(R.drawable.bg_lightgray)
                    )
                    .into(imgPoster)
                tvTitle.text = cinema.title
                tvReleaseDate.text = cinema.release
            }

            itemView.setOnClickListener {
                when (cinemaType) {
                    CinemaType.MOVIE -> {
                        val itemContext = itemView.context
                        val intent = Intent(itemContext, DetailMovieActivity::class.java)
                        intent.putExtra(DetailMovieActivity.EXTRA_ID, cinema.id)
                        itemContext.startActivity(intent)
                    }
                    CinemaType.TVSHOW -> {
                        val itemContext = itemView.context
                        val intent = Intent(itemContext, DetailTvShowActivity::class.java)
                        intent.putExtra(DetailTvShowActivity.EXTRA_ID, cinema.id)
                        itemContext.startActivity(intent)
                    }
                }
            }
        }
    }
}