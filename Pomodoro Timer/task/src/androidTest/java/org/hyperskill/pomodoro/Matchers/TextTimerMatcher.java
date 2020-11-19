package org.hyperskill.pomodoro.Matchers;

import androidx.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hyperskill.pomodoro.timer.TimerView;

public class TextTimerMatcher extends BoundedMatcher<View, TimerView> {
    private String text;

    private TextTimerMatcher(String text) {
        super(TimerView.class);
        this.text = text;
    }

    public static TextTimerMatcher withText(String text) {
        return new TextTimerMatcher(text);
    }

    @Override
    protected boolean matchesSafely(TimerView item) {
        return item.getText().equals(text);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("getText should return ").appendValue(text);
    }
}
