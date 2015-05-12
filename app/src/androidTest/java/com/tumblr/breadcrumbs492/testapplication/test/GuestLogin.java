package com.tumblr.breadcrumbs492.testapplication.test;

import com.tumblr.breadcrumbs492.testapplication.LoginActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

/** We can no longer login as guest, this test is deprecated.
public class GuestLogin extends ActivityInstrumentationTestCase2<LoginActivity> {
  	private Solo solo;
  	
  	public GuestLogin() {
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
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.LoginActivity'
		solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.LoginActivity.class, 2000);
        //Click on Log in as guest
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.login_as_guest));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Click on Add Crumb
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_3));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity.class));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name));
        //Enter the text: 'test crumb'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name), "test crumb");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment));
        //Enter the text: 'test comment'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment), "test comment");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags));
        //Enter the text: 'test tag'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags), "test tag");
        //Click on Drop Crumb
		solo.clickOnText(java.util.regex.Pattern.quote("Drop Crumb"));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageView.class, 2));
        //Click on Settings
		solo.clickInList(1, 0);
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.SettingsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.SettingsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.SettingsActivity.class));
        //Click on LinearLayout Toggle Public/Private Crumbs Make your crumbs public LinearLayout
		solo.clickInList(2, 0);
        //Click on LinearLayout Crumbs Display Radius Adjust the radius to display crumbs in a
		solo.clickInList(3, 0);
        //Wait for dialog
		solo.waitForDialogToOpen(5000);
        //Click on sample entry 2
		solo.clickOnView(solo.getView(android.R.id.text1, 1));
        //Press menu back key
		solo.goBack();
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.edittext_location));
        //Enter the text: 'csulb'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.edittext_location));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.edittext_location), "csulb");
        //Press next button
		solo.pressSoftKeyboardNextButton();
        //Click on Find
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_find));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageView.class, 2));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.LoginActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.LoginActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.LoginActivity.class));
	}
}
**/