package com.zavierdev.cinemacatalogue.ui.detailcinema

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.constant.CinemaType
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity
import com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow.DetailTvShowActivity
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailCinemaActivityTest {
    private lateinit var detailMovieActivityRule: ActivityTestRule<DetailMovieActivity>
    private lateinit var detailTvShowActivityRule: ActivityTestRule<DetailTvShowActivity>

    @Before
    fun setup() {
        detailMovieActivityRule = ActivityTestRule<DetailMovieActivity>(
            DetailMovieActivity::class.java,
            true,    // initialTouchMode
            false  // launchActivity. False to customize the intent
        );
        detailTvShowActivityRule = ActivityTestRule<DetailTvShowActivity>(
            DetailTvShowActivity::class.java,
            true,    // initialTouchMode
            false  // launchActivity. False to customize the intent
        );
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    private fun startActivityWithIntent(id: Int, type: CinemaType) {
        val intent = Intent()
        when (type) {
            CinemaType.MOVIE -> {
                intent.putExtra(DetailMovieActivity.EXTRA_ID, id)
                detailMovieActivityRule.launchActivity(intent)
            }
            CinemaType.TVSHOW -> {
                intent.putExtra(DetailTvShowActivity.EXTRA_ID, id)
                detailTvShowActivityRule.launchActivity(intent)
            }
        }
    }

    @Test
    fun moviesRenderedDataCheck() {
        startActivityWithIntent(728754, CinemaType.MOVIE)
        onView(withId(R.id.img_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
    }

    @Test
    fun tvShowsRenderedDataCheck() {
        startActivityWithIntent(99048, CinemaType.TVSHOW)
        onView(withId(R.id.img_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
    }
}