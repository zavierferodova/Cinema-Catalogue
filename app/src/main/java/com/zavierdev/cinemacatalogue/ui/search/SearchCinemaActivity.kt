package com.zavierdev.cinemacatalogue.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zavierdev.cinemacatalogue.data.model.SearchCinemaModel
import com.zavierdev.cinemacatalogue.databinding.ActivitySearchCinemaBinding
import com.zavierdev.cinemacatalogue.utils.ViewUtils
import com.zavierdev.cinemacatalogue.viewmodel.CinemaViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchCinemaActivity : AppCompatActivity() {
    private lateinit var binder: ActivitySearchCinemaBinding
    private lateinit var searchCinemaViewModel: SearchCinemaViewModel
    private lateinit var cinemaRecyclerViewAdapter: SearchCinemaRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivitySearchCinemaBinding.inflate(layoutInflater)
        setContentView(binder.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Disable dark theme

        provideViewModel()
        viewInit()
        observeSearchData()
    }

    private fun provideViewModel() {
        val viewModelFactory = CinemaViewModelFactory.getInstance(this)
        searchCinemaViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchCinemaViewModel::class.java)
    }

    private fun viewInit() {
        cinemaRecyclerViewAdapter = SearchCinemaRecyclerViewAdapter()
        binder.rvSearchCinema.apply {
            adapter = cinemaRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this@SearchCinemaActivity)
        }

        binder.edtSearchCinema.addTextChangedListener {
            loadingState()
            GlobalScope.launch {
                searchCinemaViewModel.searchCinema(binder.edtSearchCinema.text.toString())
            }
        }

        firstState()
    }

    private fun observeSearchData() {
        searchCinemaViewModel.searchData.observe(this, { searchData ->
            when {
                binder.edtSearchCinema.text.toString().isEmpty() -> {
                    renderSearchItem(ArrayList())
                    firstState()
                }
                searchData != null -> {
                    renderSearchItem(searchData)
                }
                else -> {
                    renderSearchItem(ArrayList())
                    notFoundState()
                }
            }
        })
    }

    private fun firstState() {
        ViewUtils.hideView(binder.rvSearchCinema)
        ViewUtils.hideView(binder.tvSearchNotFound)
        ViewUtils.hideView(binder.loadingSearchCinema)
        ViewUtils.hideView(binder.tvSearchConnectionError)
    }

    private fun loadingState() {
        ViewUtils.showView(binder.loadingSearchCinema)
        ViewUtils.hideView(binder.rvSearchCinema)
        ViewUtils.hideView(binder.tvSearchNotFound)
        ViewUtils.hideView(binder.tvSearchConnectionError)
    }

    private fun notFoundState() {
        ViewUtils.showView(binder.tvSearchNotFound)
        ViewUtils.hideView(binder.rvSearchCinema)
        ViewUtils.hideView(binder.loadingSearchCinema)
        ViewUtils.hideView(binder.tvSearchConnectionError)
    }

    private fun connectionErrorState() {
        ViewUtils.showView(binder.tvSearchConnectionError)
        ViewUtils.hideView(binder.tvSearchNotFound)
        ViewUtils.hideView(binder.rvSearchCinema)
        ViewUtils.hideView(binder.loadingSearchCinema)
    }

    private fun showSearchItem() {
        ViewUtils.showView(binder.rvSearchCinema)
        ViewUtils.hideView(binder.loadingSearchCinema)
        ViewUtils.hideView(binder.tvSearchNotFound)
        ViewUtils.hideView(binder.tvSearchConnectionError)
    }

    private fun renderSearchItem(searchData: ArrayList<SearchCinemaModel>) {
        showSearchItem()
        cinemaRecyclerViewAdapter.searchData = searchData
        cinemaRecyclerViewAdapter.notifyDataSetChanged()
    }
}