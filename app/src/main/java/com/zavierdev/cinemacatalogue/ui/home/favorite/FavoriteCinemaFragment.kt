package com.zavierdev.cinemacatalogue.ui.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zavierdev.cinemacatalogue.R

class FavoriteCinemaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_cinema, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cinemaViewPager: ViewPager = view.findViewById(R.id.viewpager_fav_cinema)
        val cinemaTabLayout: TabLayout = view.findViewById(R.id.tab_layout_fav_cinema)
        val favoriteCinemaViewPagerAdapter =
            activity?.let { FavoriteCinemaViewPagerAdapter(it, childFragmentManager) }

        cinemaViewPager.adapter = favoriteCinemaViewPagerAdapter
        cinemaTabLayout.setupWithViewPager(cinemaViewPager)
    }
}