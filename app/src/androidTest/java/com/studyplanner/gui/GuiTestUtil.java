package com.studyplanner.gui;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class GuiTestUtil {

    public final static String SUBJECT_NAME = "Maths";
    public final static String SUBJECT_DATE = "Mon, 22.02.21";
    public final static String SUBJECT_DATE_ENTERED = "22.2.21";
    public final static String TOPIC_NAME_OLD = "study";
    public final static String TOPIC_NAME_NEW = "Homework";
    public final static String TOPIC_DATE_ENTERED = "28.1.2021";
    public final static String TOPIC_DURATION = "60";
    public final static String TOPIC_DISPLAYED = "[Maths] study";
    public final static String TOPIC_DETAILS = "60 min | Thu, 28.01.21";

    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
