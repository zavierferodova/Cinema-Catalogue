package com.zavierdev.cinemacatalogue.ui.home.tvshow

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
import com.zavierdev.cinemacatalogue.databinding.FragmentTvShowBinding
import com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow.DetailTvShowActivity
import com.zavierdev.cinemacatalogue.ui.search.SearchCinemaActivity
import com.zavierdev.cinemacatalogue.utils.ViewUtils
import com.zavierdev.cinemacatalogue.viewmodel.TvShowViewModelFactory
import com.zavierdev.cinemacatalogue.vo.Status

class TvShowFragment : Fragment() {
    private lateinit var binder: FragmentTvShowBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tvShowViewModel: TvShowViewModel
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var rvTvShow: RecyclerView
    private lateinit var rvTvShowAdapter: TvShowRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binder = FragmentTvShowBinding.inflate(layoutInflater)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit(view)
        provideTvShowData()
    }

    private fun provideTvShowData() {
        provideViewModel()
        observeTvShows()
    }

    private fun provideViewModel() {
        val viewModelFactory = TvShowViewModelFactory.getInstance(requireActivity())
        tvShowViewModel =
            ViewModelProvider(this, viewModelFactory).get(TvShowViewModel::class.java)
    }

    private fun viewInit(view: View) {
        swipeRefreshLayout = binder.swiperefreshTvShow
        shimmerLayout = view.findViewById(R.id.shimmer_loading)
        rvTvShow = view.findViewById(R.id.rv_tv_shows)
        rvTvShowAdapter = TvShowRecyclerViewAdapter()

        swipeRefreshLayout.setOnRefreshListener {
            provideTvShowData()
            swipeRefreshLayout.isRefreshing = true
        }

        rvTvShow.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = rvTvShowAdapter
            setHasFixedSize(true)
        }

        rvTvShowAdapter.setOnClickItem(object : TvShowRecyclerViewAdapter.OnClickItem {
            override fun onClick(tvShow: DiscoverCinemaModel) {
                val intent = Intent(activity, DetailTvShowActivity::class.java)
                intent.putExtra(DetailTvShowActivity.EXTRA_ID, tvShow.id)
                startActivity(intent)
            }
        })

        binder.btnSearchTvShow.setOnClickListener {
            val intent = Intent(this.context, SearchCinemaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeTvShows() {
        tvShowViewModel.getDiscoverTvShow().observe(viewLifecycleOwner, { tvShows ->
            if (tvShows != null) {
                when (tvShows.status) {
                    Status.LOADING -> {
                        ViewUtils.hideView(rvTvShow)
                        shimmerShow(true)
                        ViewUtils.hideView(binder.containerErrorMessage.root)
                    }
                    Status.SUCCESS -> {
                        rvTvShowAdapter.tvShows = tvShows.data!!
                        rvTvShowAdapter.notifyDataSetChanged()

                        ViewUtils.showView(rvTvShow)
                        shimmerShow(false)

                        if (swipeRefreshLayout.isRefreshing)
                            swipeRefreshLayout.isRefreshing = false
                    }
                    Status.ERROR -> {
                        ViewUtils.hideView(rvTvShow)
                        shimmerShow(false)
                        ViewUtils.showView(binder.containerErrorMessage.root)
                        binder.containerErrorMessage.tvErrorMessage.text = tvShows.message

                        if (swipeRefreshLayout.isRefreshing)
                            swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        })
    }

    private fun shimmerShow(show: Boolean) {
        if (show) {
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
        } else {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
        }
    }
}