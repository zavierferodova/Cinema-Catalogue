package com.zavierdev.cinemacatalogue.ui.home.favorite

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.ui.home.favorite.movie.FavoriteMovieFragment
import com.zavierdev.cinemacatalogue.ui.home.favorite.tvshow.FavoriteTvShowFragment

class FavoriteCinemaViewPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pageTitles = listOf(R.string.title_movie, R.string.title_tv_show)

    override fun getPageTitle(position: Int): CharSequence? =
        mContext.resources.getString(pageTitles[position])

    override fun getCount(): Int = pageTitles.size

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteMovieFragment()
            1 -> FavoriteTvShowFragment()
            else -> Fragment()
        }
    }
}