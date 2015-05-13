package com.tumblr.breadcrumbs492.testapplication.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.FlakyTest;

import com.robotium.solo.Solo;
import com.tumblr.breadcrumbs492.testapplication.LoginActivity;
import com.tumblr.breadcrumbs492.testapplication.MapsActivity;
import com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity;
import com.tumblr.breadcrumbs492.testapplication.ProfileActivity;
import com.tumblr.breadcrumbs492.testapplication.R;

/**
 * Created by a4r0ng on 5/13/2015.
 */
public class MyCrumbsBackToMapsTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public MyCrumbsBackToMapsTest() {
        super(LoginActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    @FlakyTest
    public void testRun() {
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.LoginActivity'
        solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.LoginActivity.class, 20000);
        //Click on Empty Text View
        solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
        //Enter the text: 'daniel'
        solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
        solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username), "fun");
        //Click on Empty Text View
        solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
        //Enter the text: 'password1'
        solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
        solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password), "yay");
        //Press next button
        solo.pressSoftKeyboardNextButton();
        //Click on Sign in or register
        solo.clickOnView(solo.getView(R.id.signin));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
        assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class, 2000));

        solo.sleep(1000);
        solo.clickOnImageButton(1);

        assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(MyCrumbsActivity.class, 2000));
        // Log out
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarItem(R.id.go_back);
        assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(MapsActivity.class, 2000));


    }
}
