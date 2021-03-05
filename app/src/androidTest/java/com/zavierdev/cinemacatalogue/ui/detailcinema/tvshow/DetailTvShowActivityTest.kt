package com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.zavierdev.cinemacatalogue.R
import com.zavierdev.cinemacatalogue.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailTvShowActivityTest {
    private lateinit var detailTvShowActivityRule: ActivityTestRule<DetailTvShowActivity>

    @Before
    fun setup() {
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

    private fun startActivityWithIntent(id: Int) {
        val intent = Intent()
        intent.putExtra(DetailTvShowActivity.EXTRA_ID, id)
        detailTvShowActivityRule.launchActivity(intent);
    }

    @Test
    fun tvShowsRenderedDataCheck() {
        startActivityWithIntent(99048)
        Espresso.onView(ViewMatchers.withId(R.id.img_poster))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}