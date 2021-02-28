package com.zavierdev.cinemacatalogue.ui.detailcinema

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailCinemaActivityTest {
    private lateinit var activityRule: ActivityTestRule<DetailMovieActivity>

    @Before
    fun setup() {
        activityRule = ActivityTestRule<DetailMovieActivity>(
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

    private fun startActivityWithIntent(id: Int, type: String) {
        val intent = Intent()
        intent.putExtra(DetailMovieActivity.EXTRA_ID, id);
        intent.putExtra(DetailMovieActivity.EXTRA_TYPE, type)
        activityRule.launchActivity(intent)
    }

    @Test
    fun moviesRenderedDataCheck() {
        startActivityWithIntent(728754, "movie")
        onView(withId(R.id.img_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_duration)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_status)).perform(scrollTo(), click())
        onView(withId(R.id.tv_status)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_movie_activity_container)).perform(swipeUp())
        onView(withId(R.id.rv_genres_item)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_credits_item)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_overview)).check(matches(isDisplayed()))
    }

    @Test
    fun tvShowsRenderedDataCheck() {
        startActivityWithIntent(99048, "tvshow")
        onView(withId(R.id.img_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_duration)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_status)).perform(scrollTo(), click())
        onView(withId(R.id.tv_status)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_movie_activity_container)).perform(swipeUp())
        onView(withId(R.id.rv_genres_item)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_credits_item)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_overview)).check(matches(isDisplayed()))
    }
}