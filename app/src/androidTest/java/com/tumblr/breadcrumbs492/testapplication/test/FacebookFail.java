package com.tumblr.breadcrumbs492.testapplication.test;

import com.tumblr.breadcrumbs492.testapplication.LoginActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class FacebookFail extends ActivityInstrumentationTestCase2<LoginActivity> {
  	private Solo solo;
  	
  	public FacebookFail() {
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
        //Sleep for 12913 milliseconds
		solo.sleep(12913);
        //Click on Log in with Facebook
		solo.clickOnView(solo.getView(com.tumblr.breadcrumbs492.testapplication.R.id.authButton));
	}
}
