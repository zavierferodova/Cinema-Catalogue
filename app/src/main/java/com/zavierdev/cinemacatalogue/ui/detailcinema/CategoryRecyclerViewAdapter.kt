package com.zavierdev.cinemacatalogue.ui.detailcinema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.data.model.GenreCinemaModel

class CategoryRecyclerViewAdapter() :
    RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {

    var categories: ArrayList<GenreCinemaModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCategory.text = categories[position].name
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
    }
}