package com.zavierdev.cinemacatalogue.ui.detailcinema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import de.hdodenhof.circleimageview.CircleImageView

class CreditRecyclerViewAdapter : RecyclerView.Adapter<CreditRecyclerViewAdapter.ViewHolder>() {
    var credits: ArrayList<CreditCinemaModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = credits[position]

        Glide.with(holder.itemView.context)
            .load(model.playerImage)
            .apply(
                RequestOptions.placeholderOf(R.drawable.bg_lightgray)
                    .error(R.drawable.bg_lightgray)
            )
            .into(holder.imgCastPlayer)

        holder.tvPlayerName.text = model.playerName
        holder.tvCharacterName.text = model.characterName
    }

    override fun getItemCount(): Int = credits.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCastPlayer: CircleImageView = itemView.findViewById(R.id.img_cast_player)
        val tvPlayerName: TextView = itemView.findViewById(R.id.tv_player_name)
        val tvCharacterName: TextView = itemView.findViewById(R.id.tv_character_name)
    }
}