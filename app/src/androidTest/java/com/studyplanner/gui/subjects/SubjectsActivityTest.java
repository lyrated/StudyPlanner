package com.studyplanner.gui.subjects;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.studyplanner.gui.GuiTestUtil.*;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SubjectsActivityTest {

    @Rule
    public ActivityTestRule<SubjectsActivity> mActivityTestRule = new ActivityTestRule<>(SubjectsActivity.class);

    @Test
    public void onClickMenuItem() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.new_subject), withContentDescription(R.string.add_subject),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction activityTextView = onView(
                allOf(withText(R.string.add_subject),
                        isDisplayed()));
        activityTextView.check(matches(withText(R.string.add_subject)));
    }

    @Test
    public void onClickRecyclerviewItem() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.layoutItem),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction activityTextView = onView(
                allOf(withText(R.string.add_subject),
                        isDisplayed()));
        activityTextView.check(matches(withText(R.string.add_subject)));
    }

    @Test
    public void showTextInRecyclerviewItem1() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.itemTextView), withText(SUBJECT_NAME),
                        isDisplayed()));
        textView.check(matches(withText(SUBJECT_NAME)));
    }

    @Test
    public void showTextInRecyclerviewItem2() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.detailsTextView), withText(SUBJECT_DATE),
                        isDisplayed()));
        textView.check(matches(withText(SUBJECT_DATE)));
    }

}