package com.zavierdev.cinemacatalogue.ui.home.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.data.model.DiscoverCinemaModel
import com.zavierdev.cinemacatalogue.databinding.FragmentMovieBinding
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity
import com.zavierdev.cinemacatalogue.ui.search.SearchCinemaActivity
import com.zavierdev.cinemacatalogue.utils.ViewUtils
import com.zavierdev.cinemacatalogue.viewmodel.MovieViewModelFactory
import com.zavierdev.cinemacatalogue.vo.Status

class MovieFragment : Fragment() {
    private lateinit var binder: FragmentMovieBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var rvMovie: RecyclerView
    private lateinit var rvMovieAdapter: MovieRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binder = FragmentMovieBinding.inflate(layoutInflater)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit(view)
        provideMovieData()
    }

    private fun provideMovieData() {
        provideViewModel()
        observeMovies()
    }

    private fun provideViewModel() {
        val viewModelFactory = MovieViewModelFactory.getInstance(requireActivity())
        movieViewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]
    }

    private fun viewInit(view: View) {
        swipeRefreshLayout = binder.swiperefreshMovie
        shimmerLayout = view.findViewById(R.id.shimmer_loading)
        rvMovie = view.findViewById(R.id.rv_movies)
        rvMovieAdapter = MovieRecyclerViewAdapter()

        swipeRefreshLayout.setOnRefreshListener {
            provideMovieData()
            binder.swiperefreshMovie.isRefreshing = true
        }

        rvMovie.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = rvMovieAdapter
            setHasFixedSize(true)
        }

        rvMovieAdapter.setOnClickItem(object : MovieRecyclerViewAdapter.OnClickItem {
            override fun onClick(movie: DiscoverCinemaModel) {
                val intent = Intent(activity, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_ID, movie.id)
                startActivity(intent)
            }
        })

        binder.btnSearchMovie.setOnClickListener {
            val intent = Intent(this.context, SearchCinemaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeMovies() {
        movieViewModel.getDiscoverMovie().observe(viewLifecycleOwner, { movies ->
            if (movies != null) {
                when (movies.status) {
                    Status.LOADING -> {
                        ViewUtils.hideView(rvMovie)
                        shimmerShow(true)
                        ViewUtils.hideView(binder.containerErrorMessage.root)
                    }
                    Status.SUCCESS -> {
                        rvMovieAdapter.movies = movies.data!!
                        rvMovieAdapter.notifyDataSetChanged()

                        ViewUtils.showView(rvMovie)
                        shimmerShow(false) // Hide shimmer loading

                        if (swipeRefreshLayout.isRefreshing)
                            swipeRefreshLayout.isRefreshing = false
                    }
                    Status.ERROR -> {
                        ViewUtils.hideView(rvMovie)
                        shimmerShow(false)
                        ViewUtils.showView(binder.containerErrorMessage.root)
                        binder.containerErrorMessage.tvErrorMessage.text = movies.message

                        if (swipeRefreshLayout.isRefreshing)
                            swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        })
    }

    private fun shimmerShow(start: Boolean) {
        if (start) {
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
        } else {
            shimmerLayout.visibility = View.GONE
            shimmerLayout.stopShimmer()
        }
    }
}