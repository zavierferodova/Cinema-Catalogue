package com.zavierdev.cinemacatalogue.ui.detailcinema.movie

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.constant.CinemaType
import com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow.DetailTvShowActivity
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailMovieActivityTest {
    private lateinit var detailMovieActivityRule: ActivityTestRule<DetailMovieActivity>

    @Before
    fun setup() {
        detailMovieActivityRule = ActivityTestRule<DetailMovieActivity>(
            DetailMovieActivity::class.java,
            true,    // initialTouchMode
            false  // launchActivity. False to customize the intent
        );
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    private fun startActivityWithIntent(id: Int) {
        val intent = Intent()
        intent.putExtra(DetailMovieActivity.EXTRA_ID, id)
        detailMovieActivityRule.launchActivity(intent)
    }

    @Test
    fun moviesRenderedDataCheck() {
        startActivityWithIntent(728754)
        Espresso.onView(ViewMatchers.withId(R.id.img_poster))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}