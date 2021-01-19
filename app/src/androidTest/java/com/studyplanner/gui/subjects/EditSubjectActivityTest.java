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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.studyplanner.gui.GuiTestUtil.SUBJECT_NAME;
import static com.studyplanner.gui.GuiTestUtil.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditSubjectActivityTest {

    @Rule
    public ActivityTestRule<SubjectsActivity> mActivityTestRule = new ActivityTestRule<>(SubjectsActivity.class);

    @Test
    public void showDataFromItem() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.layoutItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview),
                                        0),
                                0)));
        linearLayout.perform(click());

        ViewInteraction editSubjectNameEditText = onView(
                allOf(withId(R.id.edit_subject),
                        isDisplayed()));
        editSubjectNameEditText.check(matches(withText(SUBJECT_NAME)));
    }

    @Test
    public void showDataFromItemEmpty() {
        ViewInteraction menuItem = onView(
                allOf(withId(R.id.new_subject),
                        isDisplayed()));
        menuItem.perform(click());

        ViewInteraction editSubjectNameEditText = onView(
                allOf(withId(R.id.edit_subject),
                        isDisplayed()));
        editSubjectNameEditText.check(matches(withText("")));
    }

    @Test
    public void switchedToSubjectsActivity() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.layoutItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview),
                                        0),
                                0)));
        linearLayout.perform(click());

        ViewInteraction saveButton = onView(
                allOf(withId(R.id.button_save),
                        isDisplayed()));
        saveButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withText(R.string.all_subjects),
                        isDisplayed()));
        textView.check(matches(withText(R.string.all_subjects)));
    }

}