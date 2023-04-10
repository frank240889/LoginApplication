package dev.franco.appxml.ui.login

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import dev.franco.loginapplicationxml.R
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {
    @get:Rule
    val activityScenarioRule = activityScenarioRule<LoginActivity>()

    @Before
    fun setup() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun whenActivityStartsThenCheckCorrectViewsVisibility() {
        onView(withId(R.id.iv_logo)).check(matches(isDisplayed()))
        onView(withId(R.id.til_username)).check(matches(isDisplayed()))
        onView(withId(R.id.tiet_username)).check(matches(isDisplayed()))
        onView(withId(R.id.til_password)).check(matches(isDisplayed()))
        onView(withId(R.id.tiet_password)).check(matches(isDisplayed()))
        onView(withId(R.id.fl_blocking_layer)).check(matches(not(isDisplayed())))
        onView(withId(R.id.mb_login)).check(matches(isDisplayed()))
    }

    @Test
    fun whenUserTypesUserAndPasswordThenCheckFieldsUpdatesTheirValuesAccording() {
        val user = "Franco"
        val password = "Omar"
        onView(withId(R.id.tiet_username)).perform(typeText(user))
        onView(withId(R.id.tiet_password)).perform(typeText(password))

        onView(withId(R.id.tiet_username)).check(matches(withText(user)))
        onView(withId(R.id.tiet_password)).check(matches(withText(password)))
    }

    @Test
    fun whenUserPressLoginThenResultDialogIsShown() {
        onView(withId(R.id.mb_login)).perform(click())
        onView(withText(R.string.attention))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @After
    fun cleanup() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
        activityScenarioRule.scenario.close()
    }
}
