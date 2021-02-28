package com.zavierdev.cinemacatalogue.ui.home.favorite.movie

import android.content.Intent
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
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import com.zavierdev.cinemacatalogue.utils.ViewUtils
import com.zavierdev.cinemacatalogue.viewmodel.MovieViewModelFactory

class FavoriteMovieFragment : Fragment(), FavoriteMovieRecyclerViewAdapter.MoveActivityCallback {
    private lateinit var favoriteMovieViewModel: FavoriteMovieViewModel
    private lateinit var favoriteNotFound: LinearLayout
    private lateinit var shimmerLoading: ShimmerFrameLayout
    private lateinit var rvFavoriteMovies: RecyclerView
    private lateinit var rvFavoriteMovieAdapter: FavoriteMovieRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideViewModel()
        viewInit(view)
        observeFavoriteMovie()
    }

    private fun viewInit(view: View) {
        favoriteNotFound = view.findViewById(R.id.favorite_not_found_movie)
        shimmerLoading = view.findViewById(R.id.shimmer_loading)
        rvFavoriteMovies = view.findViewById(R.id.rv_favorite_movies)
        rvFavoriteMovieAdapter = FavoriteMovieRecyclerViewAdapter(this)

        rvFavoriteMovies.apply {
            adapter = rvFavoriteMovieAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        ViewUtils.showView(shimmerLoading, true)
        ViewUtils.showView(rvFavoriteMovies, false)
    }

    private fun provideViewModel() {
        val viewModelFactory = MovieViewModelFactory.getInstance(requireActivity())
        favoriteMovieViewModel =
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[FavoriteMovieViewModel::class.java]
    }

    private fun observeFavoriteMovie() {
        favoriteMovieViewModel.getFavoriteMovies().observe(viewLifecycleOwner, { movies ->
            ViewUtils.showView(shimmerLoading, false)

            EspressoIdlingResource.increment()

            if (movies.isNullOrEmpty()) {
                ViewUtils.showView(favoriteNotFound, true)
                ViewUtils.showView(rvFavoriteMovies, false)
            } else {
                rvFavoriteMovieAdapter.submitList(movies)
                ViewUtils.showView(favoriteNotFound, false)
                ViewUtils.showView(rvFavoriteMovies, true)
            }

            EspressoIdlingResource.decrement()
        })
    }

    override fun onClick(model: MovieEntity) {
        val intent = Intent(activity, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_ID, model.id)
        startActivity(intent)
    }
}