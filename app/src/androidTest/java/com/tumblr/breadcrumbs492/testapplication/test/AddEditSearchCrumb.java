package com.tumblr.breadcrumbs492.testapplication.test;

import com.tumblr.breadcrumbs492.testapplication.LoginActivity;
import com.robotium.solo.*;
import com.tumblr.breadcrumbs492.testapplication.R;

import android.test.ActivityInstrumentationTestCase2;


public class AddEditSearchCrumb extends ActivityInstrumentationTestCase2<LoginActivity> {
  	private Solo solo;
  	
  	public AddEditSearchCrumb() {
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
		assertTrue("com.tumblr.breadcrumbs492.testapplication.LoginActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.LoginActivity.class));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
        //Enter the text: 'scotttack'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username), "scotttack");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
        //Enter the text: 'password1'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_password), "password1");
        //Press next button
		solo.pressSoftKeyboardNextButton();
        //Click on SIGN IN
		solo.clickOnView(solo.getView(R.id.signin));
        //Click on SIGN IN
		solo.clickOnView(solo.getView(R.id.signin));
        //Click on scotttack
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
        //Enter the text: 'scotttak'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.enter_username), "scotttak");
        //Click on SIGN IN
		solo.clickOnView(solo.getView(R.id.signin));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Click on android.view.TextureView
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ImageView
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_3));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity.class));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name));
        //Enter the text: 'Cecs492'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_name), "Cecs492");
        //Enter the text: 'CS is fun!'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_comment), "CS is fun!");
        //Enter the text: '492, test'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.addcrumb_tags), "492, test");
        //Click on Drop Crumb
		solo.clickOnText(java.util.regex.Pattern.quote("Drop Crumb"));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Click on android.view.TextureView
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity.class));
        //Click on Cecs492 2015-05-11 19:58:03.0
		solo.clickInList(8, 0);
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.EditCrumb'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.EditCrumb is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.EditCrumb.class));
        //Click on android.view.TextureView
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on android.view.TextureView
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on CS is fun!
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.editcrumb_description));
        //Enter the text: 'CS is cool'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.editcrumb_description));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.editcrumb_description), "CS is cool");
        //Click on Save Changes
		solo.clickOnText(java.util.regex.Pattern.quote("Save Changes"));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.edittext_location));
        //Enter the text: '492'
		solo.clearEditText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.edittext_location));
		solo.enterText((android.widget.EditText) solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.edittext_location), "492");
        //Click on Find
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_find));
        //Click on View All
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.button_display_all));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.SearchResults'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.SearchResults is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.SearchResults.class));
        //Click on 2015-05-11 19:58:03.0
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.crumbDate, 2));
        //Click on 3 Cecs492 2015-05-11 19:58:03.0 Dropped by: scotttak 0
		solo.clickInList(3, 0);
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.CrumbDetails'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.CrumbDetails is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.CrumbDetails.class));
        //Click on ImageView
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.buttonVote));
        //Click on android.view.TextureView
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on android.view.TextureView
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on Back
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.go_back));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.SearchResults'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.SearchResults is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.SearchResults.class));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("com.tumblr.breadcrumbs492.testapplication.MapsActivity is not found!", solo.waitForActivity(com.tumblr.breadcrumbs492.testapplication.MapsActivity.class));
	}
}
