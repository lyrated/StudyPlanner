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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
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
public class TopicsActivityTest {

    @Rule
    public ActivityTestRule<TopicsActivity> mActivityTestRule = new ActivityTestRule<>(TopicsActivity.class);

    @Test
    public void onClickMenuStudyPartner() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.study_partner),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withText(R.string.study_partner),
                        isDisplayed())
        );
        textView.check(matches(withText(R.string.study_partner)));
    }

    @Test
    public void onClickMenuSubjects() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.all_subjects),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withText(R.string.all_subjects),
                        isDisplayed())
        );
        textView.check(matches(withText(R.string.all_subjects)));
    }

    @Test
    public void onClickMenuNewTopic() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.new_topic),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withText(R.string.add_topic),
                        childAtPosition(withId(R.id.action_bar),
                                0),
                        isDisplayed()));
        textView.check(matches(withText(R.string.add_topic)));
    }

    @Test
    public void onClickFab() {
        ViewInteraction noSubjectsButton = onView(
                allOf(withId(R.id.emptyView), withText(R.string.no_subjects),
                        isDisplayed())
        );

        if (noSubjectsButton == null) {
            ViewInteraction fabButton = onView(
                    allOf(withId(R.id.fab),
                            isDisplayed()));
            fabButton.perform(click());

            ViewInteraction textView = onView(
                    allOf(withText(R.string.add_topic),
                            childAtPosition(withId(R.id.action_bar),
                                    0),
                            isDisplayed()));
            textView.check(matches(withText(R.string.add_topic)));
        }
    }

    @Test
    public void onClickRecyclerviewItem() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.layoutItem),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction activityTextView = onView(
                allOf(withText(R.string.add_topic),
                        childAtPosition(withId(R.id.action_bar),
                                0),
                        isDisplayed()));
        activityTextView.check(matches(withText(R.string.add_topic)));
    }

    @Test
    public void showTextInRecyclerviewItem1() {
        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.new_topic), withContentDescription(R.string.add_topic),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edit_topic),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText(TOPIC_NAME_OLD), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_date),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText(TOPIC_DATE_ENTERED), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edit_duration),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText(TOPIC_DURATION), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.button_save_topic), withText(R.string.button_save),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.itemTextView), withText(TOPIC_DISPLAYED_OLD),
                        isDisplayed()));
        textView.check(matches(withText(TOPIC_DISPLAYED_OLD)));
    }

    @Test
    public void showTextInRecyclerviewItem2() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.detailsTextView), withText(TOPIC_DETAILS),
                        isDisplayed()));
        textView.check(matches(withText(TOPIC_DETAILS)));
    }

}