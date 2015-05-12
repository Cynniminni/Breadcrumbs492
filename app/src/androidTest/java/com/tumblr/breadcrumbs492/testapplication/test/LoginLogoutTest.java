package com.tumblr.breadcrumbs492.testapplication.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.FlakyTest;
import android.test.InstrumentationTestCase;

import com.robotium.solo.Solo;
import com.tumblr.breadcrumbs492.testapplication.LoginActivity;
import com.tumblr.breadcrumbs492.testapplication.R;
import com.tumblr.breadcrumbs492.testapplication.SettingsActivity;

/**
 * Created by Aaron on 5/11/2015.
 */
public class LoginLogoutTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public LoginLogoutTest() {
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
        solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.signin_or_register));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
        assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class, 2000));
        // Log out
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarItem(R.id.action_logout);
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.LoginActivity'
        //
        assertTrue("com.tumblr.breadcrumbs492.testapplication.LoginActivity is not found!", solo.waitForActivity(LoginActivity.class));

        // Log back in
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
        solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.signin_or_register));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
        assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class, 2000));

    }
}

