package org.hyperskill.pomodoro;

import android.graphics.Color;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hyperskill.pomodoro.Matchers.ColorTimerMatcher;
import org.hyperskill.pomodoro.Matchers.TextTimerMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TimerStateInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void timerViewExist() {
        onView(withId(R.id.timerView)).perform(ViewActions.click());
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:05")));
    }

    @Test
    public void startTimer() throws InterruptedException {
        onView(withId(R.id.startButton)).perform(ViewActions.click());
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.RED)));
        Thread.sleep(5000);
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.GREEN)));
        Thread.sleep(10000);
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.YELLOW)));
    }

    @Test
    public void resetTimer() throws InterruptedException {
        onView(withId(R.id.startButton)).perform(ViewActions.click());
        Thread.sleep(4000);
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:00")));
        onView(withId(R.id.resetButton)).perform(ViewActions.click());
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:05")));
    }

    @Test
    public void interruptTimer() throws InterruptedException {
        onView(withId(R.id.startButton)).perform(ViewActions.click());
        Thread.sleep(2000);
        startTimer();
    }

}