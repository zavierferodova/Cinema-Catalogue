package com.zavierdev.cinemacatalogue.ui.tvshow

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zavierdev.cinemacatalogue.ui.home.HomeActivity
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class TvShowFragmentTest {
    @Before
    fun setup() {
        ActivityScenario.launch(HomeActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    private fun openTvShowFragment() {
        onView(ViewMatchers.withId(R.id.navigation_tvshow)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.rv_tv_shows))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun loadTvShow() {
        openTvShowFragment()

        onView(ViewMatchers.withId(R.id.rv_tv_shows))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.rv_tv_shows)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }

    @Test
    fun openDetailTvShow() {
        val randomValue: Int = Random.nextInt(0, 10)
        openTvShowFragment()

        onView(ViewMatchers.withId(R.id.rv_tv_shows))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.rv_tv_shows)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                randomValue,
                ViewActions.click()
            )
        )
        onView(ViewMatchers.withId(R.id.detail_tvshow_activity_container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun openMovieFragment() {
        onView(ViewMatchers.withId(R.id.navigation_movie)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.rv_movies)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}