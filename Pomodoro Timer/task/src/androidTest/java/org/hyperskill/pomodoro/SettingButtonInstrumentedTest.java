package org.hyperskill.pomodoro;

import android.graphics.Color;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hyperskill.pomodoro.Matchers.ColorTimerMatcher;
import org.hyperskill.pomodoro.Matchers.TextTimerMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingButtonInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void settingButtonExist() {
        onView(withId(R.id.settingButton)).check(matches(isDisplayed()));
    }

    @Test
    public void openDialog() {
        onView(withId(R.id.settingButton)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }

    @Test
    public void setWorkTime() throws InterruptedException {
        onView(withId(R.id.settingButton)).perform(click());
        onView(ViewMatchers.withId(R.id.workTime)).perform(typeText("6"));
        Thread.sleep(2000);
        onView(withText("OK")).inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:06")));
        onView(withId(R.id.startButton)).perform(click());
        Thread.sleep(6000);
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.GREEN)));
    }

    @Test
    public void setRestTime() throws InterruptedException {
        onView(withId(R.id.settingButton)).perform(click());
        onView(withId(R.id.restTime)).perform(typeText("5"));
        Thread.sleep(2000);
        onView(withText("OK")).inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.startButton)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:00")));
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.GREEN)));
    }
}
