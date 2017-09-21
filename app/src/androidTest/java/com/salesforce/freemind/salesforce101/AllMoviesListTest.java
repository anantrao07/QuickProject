package com.salesforce.freemind.salesforce101;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.salesforce.freemind.salesforce101.ui.Movies;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by anant on 2017-09-20.
 */

@RunWith(AndroidJUnit4.class)
public final class AllMoviesListTest {


    @Rule
    public ActivityTestRule<Movies> mActivityRule = new ActivityTestRule<>(
            Movies.class);
    @Test
    public void testMoviesClick(){


        //onView(withId(R.id.get_movies_btn)).perform(click());


       // onView(withId(R.id.all_movies_recycler_view)).perform(RecyclerViewActions.action);

//need to configure espresso-contrib in gradle and perform ui testing on recycler view .


    }


}
