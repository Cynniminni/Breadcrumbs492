package com.tumblr.breadcrumbs492.testapplication.test;

import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


@SuppressWarnings("rawtypes")
public class AddCrumbActivityTest extends ActivityInstrumentationTestCase2 {
  	private Solo solo;
  	
  	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.tumblr.breadcrumbs492.testapplication.LoginActivity";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
    }
  	
  	@SuppressWarnings("unchecked")
    public AddCrumbActivityTest() throws ClassNotFoundException {
        super(launcherActivityClass);
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
        //Take screenshot
        solo.takeScreenshot();
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.LoginActivity'
		solo.waitForActivity("LoginActivity", 2000);
        //Click on Empty Text View
		solo.clickOnView(solo.getView("enter_username"));
        //Enter the text: 'daniel'
		solo.clearEditText((android.widget.EditText) solo.getView("enter_username"));
		solo.enterText((android.widget.EditText) solo.getView("enter_username"), "daniel");
        //Click on Empty Text View
		solo.clickOnView(solo.getView("enter_password"));
        //Enter the text: 'password1'
		solo.clearEditText((android.widget.EditText) solo.getView("enter_password"));
		solo.enterText((android.widget.EditText) solo.getView("enter_password"), "password1");
        //Press next button
		solo.pressSoftKeyboardNextButton();
        //Click on Sign in or register
		solo.clickOnView(solo.getView("signin_or_register"));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("MapsActivity is not found!", solo.waitForActivity("MapsActivity"));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on Add Crumb
		solo.clickOnView(solo.getView("button_3"));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity'
		assertTrue("AddCrumbActivity is not found!", solo.waitForActivity("AddCrumbActivity"));
        //Click on Empty Text View
		solo.clickOnView(solo.getView("addcrumb_name"));
        //Enter the text: 'robotium2'
		solo.clearEditText((android.widget.EditText) solo.getView("addcrumb_name"));
		solo.enterText((android.widget.EditText) solo.getView("addcrumb_name"), "robotium2");
        //Click on Empty Text View
		solo.clickOnView(solo.getView("addcrumb_tags"));
        //Set default small timeout to 12231 milliseconds
		Timeout.setSmallTimeout(12231);
        //Enter the text: 'robotium, test, 492'
		solo.clearEditText((android.widget.EditText) solo.getView("addcrumb_tags"));
		solo.enterText((android.widget.EditText) solo.getView("addcrumb_tags"), "robotium, test, 492");
        //Click on Drop Crumb
		solo.clickOnText(java.util.regex.Pattern.quote("Drop Crumb"));
        //Wait for activity: 'com.tumblr.breadcrumbs492.testapplication.MapsActivity'
		assertTrue("MapsActivity is not found!", solo.waitForActivity("MapsActivity"));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
        //Click on ab
		solo.clickOnView(solo.getView(android.view.TextureView.class, 0));
	}
}
