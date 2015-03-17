package com.tumblr.breadcrumbs492.testapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;


public class SettingsActivity extends Activity {
    /*
        Insert the SettingsFragment class into this activity.
        This will be launched whenever the "Settings" option in the
        overflow menu is selected in other Activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }

    /*
        Load the action bar at the top.
        [Issue]:    Doesn't work... trying to add the ToolBar widget ends up overlapping the
                    settings.
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    /*
        Static class for reloading preferences every time a change is made.
     */
    public static class Settings {
        public static void loadSettings(Activity activity) {
            //SharedPreferences contains all the values of each preference
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(activity);

            /*
                Handle when user sets public/private crumbs
             */
            //get boolean value from public crumbs checkbox
            boolean publicCrumbsPreference =
                    sharedPreferences.getBoolean("public_crumbs_preference", true);

            if (publicCrumbsPreference) {
                //show message if preference is true
                Toast.makeText(activity.getApplicationContext(),
                        "Crumbs set to public", Toast.LENGTH_SHORT).show();

                //make crumbs public...
            } else {
                //show message if preference is false
                Toast.makeText(activity.getApplicationContext(),
                        "Crumbs set to private", Toast.LENGTH_SHORT).show();

                //make crumbs private...
            }

            /*
                Handle when user sets crumbs display radius
             */
            String crumbsDisplayRadius =
                    sharedPreferences.getString("display_crumbs_radius", "no selection");
            Toast.makeText(activity.getApplicationContext(),
                    "Radius = " + crumbsDisplayRadius, Toast.LENGTH_SHORT).show();
        }
    }
}
