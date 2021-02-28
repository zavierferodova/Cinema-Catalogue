package com.zavierdev.cinemacatalogue.ui.detailcinema.movie

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.constant.CinemaType
import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DetailCinemaModel
import com.zavierdev.cinemacatalogue.data.model.FavoriteCinemaModel
import com.zavierdev.cinemacatalogue.data.model.SimilarCinemaModel
import com.zavierdev.cinemacatalogue.databinding.ActivityDetailMovieBinding
import com.zavierdev.cinemacatalogue.ui.detailcinema.CategoryRecyclerViewAdapter
import com.zavierdev.cinemacatalogue.ui.detailcinema.CreditRecyclerViewAdapter
import com.zavierdev.cinemacatalogue.ui.detailcinema.SimilarRecyclerViewAdapter
import com.zavierdev.cinemacatalogue.utils.ViewUtils
import com.zavierdev.cinemacatalogue.viewmodel.MovieViewModelFactory
import com.zavierdev.cinemacatalogue.vo.Status
import java.util.concurrent.Executors

class DetailMovieActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID: String = "DETAIL_MOVIE_ACTIVITY_EXTRA_ID"
    }

    private lateinit var binder: ActivityDetailMovieBinding
    private lateinit var detailMovieViewModel: DetailMovieViewModel
    private var cinemaId: Int = 0
    private var stateFavorite: Boolean = false

    private lateinit var rvCategoryAdapter: CategoryRecyclerViewAdapter
    private lateinit var rvCreditAdapter: CreditRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binder.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Disable dark theme

        cinemaId = intent.getIntExtra(EXTRA_ID, 0)
        provideMovieData()
    }

    private fun provideMovieData() {
        provideViewModel()
        fetchAndObserveData()
    }

    private fun provideViewModel() {
        val viewModelFactory = MovieViewModelFactory.getInstance(this)
        detailMovieViewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailMovieViewModel::class.java)
    }

    private fun fetchAndObserveData() {
        detailMovieViewModel.getFavoriteMovie(cinemaId).observe(this, { movie ->
            if (movie != null) {
                setFavoriteState(true)
            }
        })

        detailMovieViewModel.getRemoteDetailMovie(cinemaId).observe(this, { cinema ->
            if (cinema != null) {
                when (cinema.status) {
                    Status.LOADING -> {
                        ViewUtils.showView(binder.loadingLayout)
                        ViewUtils.hideView(binder.activityDetailCinemaContent)
                        ViewUtils.hideView(binder.containerErrorMessage.root)
                    }
                    Status.SUCCESS -> {
                        componentRender(cinema.data!!)
                        ViewUtils.hideView(binder.loadingLayout)
                        ViewUtils.showView(binder.activityDetailCinemaContent)
                        ViewUtils.hideView(binder.containerErrorMessage.root)
                        scrollToTopContent()

                        if (binder.swiperefreshDetailMovie.isRefreshing) {
                            binder.swiperefreshDetailMovie.isRefreshing = false
                        }
                    }
                    Status.ERROR -> {
                        ViewUtils.hideView(binder.loadingLayout)
                        ViewUtils.hideView(binder.activityDetailCinemaContent)
                        ViewUtils.showView(binder.containerErrorMessage.root)
                        binder.containerErrorMessage.tvErrorMessage.text = cinema.message

                        if (binder.swiperefreshDetailMovie.isRefreshing) {
                            binder.swiperefreshDetailMovie.isRefreshing = false
                        }
                    }
                }
            }
        })

        detailMovieViewModel.getRemoteCreditsMovie(cinemaId).observe(this, { credits ->
            if (credits != null) {
                when (credits.status) {
                    Status.LOADING -> renderCredits(ArrayList())
                    Status.SUCCESS -> renderCredits(credits.data!!)
                    Status.ERROR -> renderCredits(ArrayList())
                }
            }
        })

        detailMovieViewModel.getSimilarMovies(cinemaId).observe(this, { similarMovies ->
            if (similarMovies != null) {
                when (similarMovies.status) {
                    Status.LOADING -> renderSimilarMovies(ArrayList())
                    Status.SUCCESS -> renderSimilarMovies(similarMovies.data!!)
                    Status.ERROR -> renderSimilarMovies(ArrayList())
                }
            }
        })
    }

    private fun scrollToTopContent() {
        Handler(mainLooper).postDelayed({
            binder.detailMovieActivityContainer.smoothScrollTo(
                0,
                0
            )
        }, 250)
    }

    @SuppressLint("SetTextI18n")
    private fun componentRender(model: DetailCinemaModel) {
        val coordinatorLayout = binder.detailMovieActivityCoordinatorLayout
        val rvCategories: RecyclerView = binder.rvGenresItem

        binder.swiperefreshDetailMovie.setOnRefreshListener {
            provideMovieData()
            binder.swiperefreshDetailMovie.isRefreshing = true
        }

        rvCategoryAdapter = CategoryRecyclerViewAdapter()
        rvCategories.apply {
            adapter = rvCategoryAdapter
            layoutManager = LinearLayoutManager(
                this@DetailMovieActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        binder.btnBack.setOnClickListener {
            finish()
        }

        binder.btnFavorite.setOnClickListener {
            val favoriteCinema = FavoriteCinemaModel(
                model.id,
                model.title,
                model.release,
                model.overview,
                model.poster
            )

            if (stateFavorite) {
                detailMovieViewModel.deleteFavoriteCinema(favoriteCinema)
                setFavoriteState(false)
                Snackbar.make(
                    coordinatorLayout,
                    R.string.success_remove_favorite,
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                detailMovieViewModel.insertFavoriteCinema(favoriteCinema)
                setFavoriteState(true)
                Snackbar.make(
                    coordinatorLayout,
                    R.string.success_add_favorite,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        Glide.with(this)
            .load(model.poster)
            .apply(
                RequestOptions.placeholderOf(R.drawable.bg_lightgray)
                    .error(R.drawable.bg_lightgray)
            )
            .into(binder.imgPoster)

        binder.tvTitle.text = model.title
        binder.tvReleaseDate.text = model.release
        binder.tvDuration.text = model.runtime.toString() + " " + getString(R.string.minutes)
        binder.tvStatus.text = model.status
        binder.tvOverview.text = model.overview

        rvCategoryAdapter.categories = model.genres
        rvCategoryAdapter.notifyDataSetChanged()

        val isImageNull = model.poster.takeLast(4) == "null"
        if (!isImageNull) {
            swatchRender(model)
        }
    }

    private fun renderCredits(credits: ArrayList<CreditCinemaModel>) {
        val rvCredits: RecyclerView = binder.rvCreditsItem

        rvCreditAdapter = CreditRecyclerViewAdapter()
        rvCredits.apply {
            adapter = rvCreditAdapter
            layoutManager = LinearLayoutManager(
                this@DetailMovieActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        if (credits.isNotEmpty()) {
            rvCreditAdapter.credits = credits
            rvCreditAdapter.notifyDataSetChanged()
            ViewUtils.showView(binder.cardCredits, true)
        } else
            ViewUtils.showView(binder.cardCredits, false)
    }

    private fun renderSimilarMovies(similarMovies: ArrayList<SimilarCinemaModel>) {
        val rvSimilarMovies: RecyclerView = binder.rvSimilarMovies
        val rvSimilarMoviesAdapter = SimilarRecyclerViewAdapter(CinemaType.MOVIE)

        rvSimilarMovies.apply {
            adapter = rvSimilarMoviesAdapter
            layoutManager = LinearLayoutManager(
                this@DetailMovieActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        if (similarMovies.isNotEmpty()) {
            rvSimilarMoviesAdapter.similarCinemas = similarMovies
            rvSimilarMoviesAdapter.notifyDataSetChanged()
            ViewUtils.showView(rvSimilarMovies)
        } else {
            ViewUtils.hideView(rvSimilarMovies)
        }
    }

    private fun swatchRender(model: DetailCinemaModel) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            // Get color pallete from Poster Image
            val futureBitmap: FutureTarget<Bitmap> = Glide.with(applicationContext)
                .asBitmap()
                .load(model.poster)
                .submit();

            val posterBitmap: Bitmap = futureBitmap.get()
            val vibrantSwatch = posterBitmap.let { createPaletteSync(it).vibrantSwatch }
            val lightVibrantSwatch = posterBitmap.let { createPaletteSync(it).lightVibrantSwatch }

            // Set status bar color from pallete
            window.statusBarColor = vibrantSwatch?.rgb ?: ContextCompat.getColor(
                this,
                R.color.design_default_color_background
            )

            // Set container color from pallete
            binder.detailMovieActivityContainer.setBackgroundColor(
                lightVibrantSwatch?.rgb ?: ContextCompat.getColor(
                    this,
                    R.color.design_default_color_background
                )
            )
        }
    }

    private fun setFavoriteState(state: Boolean) {
        if (state) {
            this.stateFavorite = true
            binder.btnFavorite.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_baseline_favorite_white_24,
                null
            )
        } else {
            this.stateFavorite = false
            binder.btnFavorite.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_baseline_favorite_border_white_24,
                null
            )
        }
    }

    // Get pallete from bitmap with Synchronized
    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()
}