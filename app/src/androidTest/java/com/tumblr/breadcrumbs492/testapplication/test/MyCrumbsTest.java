package com.tumblr.breadcrumbs492.testapplication.test;

import com.tumblr.breadcrumbs492.testapplication.LoginActivity;
import com.robotium.solo.*;
import com.tumblr.breadcrumbs492.testapplication.R;

import android.test.ActivityInstrumentationTestCase2;


public class MyCrumbsTest extends ActivityInstrumentationTestCase2<LoginActivity> {
  	private Solo solo;
  	
  	public MyCrumbsTest() {
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
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
        //Enter the text: 'daniel'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username), "daniel");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
        //Enter the text: 'password1'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password), "password1");
        //Press next button
		solo.pressSoftKeyboardNextButton();
        //Click on Sign in or register
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.signin));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Set default small timeout to 10137 milliseconds
		Timeout.setSmallTimeout(10137);
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on My Crumbs
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_2));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity.class));
        //Click on robotium2
		solo.clickOnView(solo.getView(android.R.id.text1));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.EditCrumb'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.EditCrumb is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.EditCrumb.class));
        //Click on hi
		solo.clickOnView(solo.getView(R.id.editcrumb_description));
        //Press menu back key
		solo.goBack();
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity.class));
        //Press menu back key
		solo.goBack();
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Click on Add Crumb
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_3));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity.class));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name));
        //Enter the text: 'test2'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name), "test2");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment));
        //Enter the text: 'robotium'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment), "robotium");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags));
        //Enter the text: 'test, tag'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags), "test, tag");
        //Click on Drop Crumb
		solo.clickOnText(java.util.regex.Pattern.quote("Drop Crumb"));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Click on My Crumbs
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_2));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity.class));
        //Click on test2
		solo.clickOnView(solo.getView(android.R.id.text1, 3));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.EditCrumb'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.EditCrumb is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.EditCrumb.class));
        //Press menu back key
		solo.goBack();
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity.class));
        //Press menu back key
		solo.goBack();
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
	}
}
