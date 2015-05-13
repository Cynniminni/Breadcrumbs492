package com.tumblr.breadcrumbs492.testapplication.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.tumblr.breadcrumbs492.testapplication.LoginActivity;
import com.tumblr.breadcrumbs492.testapplication.R;

/**
 * Created by Aphrodite on 5/13/2015.
 */
public class LogoutAddCrumbsTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public LogoutAddCrumbsTest() {
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

    public void testRun() {
        //Sign in as cnguyen
        solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.LoginActivity.class, 20000);
        solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
        solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
        solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username), "cnguyen");
        solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
        solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
        solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password), "cnguyen");
        solo.pressSoftKeyboardNextButton();
        solo.clickOnView(solo.getView(R.id.signin));
        assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class, 2000));

        //Go to Add Crumb
        solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_3));
        assertTrue("com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity.class));
        // Log out from Add Crumb
        solo.clickOnActionBarHomeButton();
        solo.clickOnActionBarItem(R.id.action_logout_add_crumb);
    }
}
