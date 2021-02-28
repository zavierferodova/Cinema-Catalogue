package com.zavierdev.cinemacatalogue.ui.home.favorite.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import com.zavierdev.cinemacatalogue.utils.ViewUtils
import com.zavierdev.cinemacatalogue.viewmodel.TvShowViewModelFactory

class FavoriteTvShowFragment : Fragment() {
    private lateinit var tvShowViewModel: FavoriteTvShowViewModel
    private lateinit var favoriteNotFound: LinearLayout
    private lateinit var shimmerLoading: ShimmerFrameLayout
    private lateinit var rvFavoriteTvShows: RecyclerView
    private lateinit var rvFavoriteTvShowAdapter: FavoriteTvShowRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideViewModel()
        viewInit(view)
        observeFavoriteTvShow()
    }

    private fun viewInit(view: View) {
        favoriteNotFound = view.findViewById(R.id.favorite_not_found_tv_show)
        shimmerLoading = view.findViewById(R.id.shimmer_loading)
        rvFavoriteTvShows = view.findViewById(R.id.rv_favorite_tv_shows)
        rvFavoriteTvShowAdapter = FavoriteTvShowRecyclerViewAdapter()

        rvFavoriteTvShows.apply {
            adapter = rvFavoriteTvShowAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        ViewUtils.showView(shimmerLoading, true)
        ViewUtils.showView(rvFavoriteTvShows, false)
    }

    private fun provideViewModel() {
        val viewModelFactory = TvShowViewModelFactory.getInstance(requireActivity())
        tvShowViewModel =
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[FavoriteTvShowViewModel::class.java]
    }

    private fun observeFavoriteTvShow() {
        tvShowViewModel.getFavoriteTvShows().observe(viewLifecycleOwner, { tvShows ->
            ViewUtils.showView(shimmerLoading, false)

            EspressoIdlingResource.increment()

            if (tvShows.isNotEmpty()) {
                rvFavoriteTvShowAdapter.submitList(tvShows)
                ViewUtils.showView(favoriteNotFound, false)
                ViewUtils.showView(rvFavoriteTvShows, true)
            } else {
                ViewUtils.showView(favoriteNotFound, true)
                ViewUtils.showView(rvFavoriteTvShows, false)
            }

            EspressoIdlingResource.decrement()
        })
    }
}