package com.studyplanner.gui.topics;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.studyplanner.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.studyplanner.gui.GuiTestUtil.*;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditTopicActivityTest {

    @Rule
    public ActivityTestRule<TopicsActivity> mActivityTestRule = new ActivityTestRule<>(TopicsActivity.class);

    @Test
    public void showDataFromItem() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.layoutItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topic_future_recyclerview),
                                        0),
                                0)));
        linearLayout.perform(scrollTo(), click());

        ViewInteraction editTopicNameEditText = onView(
                allOf(withId(R.id.edit_topic),
                        isDisplayed()));
        editTopicNameEditText.check(matches(withText(TOPIC_NAME_OLD)));
    }

    @Test
    public void showDataFromItemEmpty() {
        ViewInteraction menuItem = onView(
                allOf(withId(R.id.new_topic),
                        isDisplayed()));
        menuItem.perform(click());

        ViewInteraction editTopicNameEditText = onView(
                allOf(withId(R.id.edit_topic),
                        isDisplayed()));
        editTopicNameEditText.check(matches(withText("")));
    }

    @Test
    public void onClickSave() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.layoutItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.topic_future_recyclerview),
                                        0),
                                0)));
        linearLayout.perform(scrollTo(), click());

        ViewInteraction saveButton = onView(
                allOf(withId(R.id.button_save_topic),
                        isDisplayed()));
        saveButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withText(R.string.app_name),
                        isDisplayed()));
        textView.check(matches(withText(R.string.app_name)));
    }

}