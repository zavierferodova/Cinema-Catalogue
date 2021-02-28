package com.zavierdev.cinemacatalogue.ui.favorite

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.ui.home.HomeActivity
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteCinemaFragmentTest {
    @Before
    fun setup() {
        ActivityScenario.launch(HomeActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    private fun openFavoriteMovie() {
        onView(withId(R.id.navigation_favorite)).perform(click())
    }

    private fun openFavoriteTvShow() {
        onView(withId(R.id.navigation_favorite)).perform(click())
        onView(withId(R.id.viewpager_fav_cinema)).perform(swipeLeft())
    }

    @Test
    fun checkFavoriteMovie() {
        // Open first movie
        onView(withId(R.id.rv_movies)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movies)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        // Action click favorite button and back button
        onView(withId(R.id.btn_favorite)).perform(click())
        onView(withId(R.id.btn_back)).perform(click())

        // Open favorite movie and check recyclerview isDisplayed
        openFavoriteMovie()
        onView(withId(R.id.rv_favorite_movies)).check(matches(isDisplayed()))

        // Open detail favorite movie and click favorite button to delete favorite movie
        onView(withId(R.id.rv_favorite_movies)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.btn_favorite)).perform(click())

        // Press back button and check favorite_not_found_movie layout is displayed
        onView(withId(R.id.btn_back)).perform(click())
        onView(withId(R.id.favorite_not_found_movie)).check(matches(isDisplayed()))
    }

    @Test
    fun checkFavoriteTvShow() {
        // Open TvShow Fragment
        onView(withId(R.id.navigation_tvshow)).perform(click())
        onView(withId(R.id.rv_tv_shows))
            .check(matches(isDisplayed()))

        // Open first tv show
        onView(withId(R.id.rv_tv_shows)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_tv_shows)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        // Action click favorite button and back button
        onView(withId(R.id.btn_favorite)).perform(click())
        onView(withId(R.id.btn_back)).perform(click())

        // Open favorite tv show and check recyclerview isDisplayed
        openFavoriteTvShow()
        onView(withId(R.id.rv_favorite_tv_shows)).check(matches(isDisplayed()))

        // Open detail favorite tv show and click favorite button to delete favorite tv show
        Thread.sleep(1000)
        onView(withId(R.id.rv_favorite_tv_shows)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.btn_favorite)).perform(click())

        // Press back button and check favorite_not_found_tv_show layout is displayed
        onView(withId(R.id.btn_back)).perform(click())
        onView(withId(R.id.favorite_not_found_tv_show)).check(matches(isDisplayed()))
    }
}