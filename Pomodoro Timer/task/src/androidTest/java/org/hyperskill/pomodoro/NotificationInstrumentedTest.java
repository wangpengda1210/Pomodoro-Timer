package org.hyperskill.pomodoro;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NotificationInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sendNotification() throws InterruptedException {
        onView(withText("Start")).perform(ViewActions.click());
        Thread.sleep(6000);
        String expectedContent = "It's time to stop";
        String expectedTitle = "You need a rest!!!";

        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        uiDevice.openNotification();
        uiDevice.wait(Until.hasObject(By.pkg("com.android.systemui")), 10000);
        UiObject2 title = uiDevice.findObject(By.text(expectedTitle));
        UiObject2 text = uiDevice.findObject(By.textStartsWith(expectedContent));

        assertEquals(expectedTitle, title.getText());
        assertTrue(text.getText().startsWith(expectedContent));

        String clearButtonText = mActivityRule.getActivity().getString(R.string.clear_notifications);
        UiObject2 clearAll = uiDevice.findObject(By.text(clearButtonText));
        clearAll.click();
    }
}
