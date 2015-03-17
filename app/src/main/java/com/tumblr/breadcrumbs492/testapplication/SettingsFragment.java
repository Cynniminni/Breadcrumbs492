package com.tumblr.breadcrumbs492.testapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

/*
    This class sole purpose is to load the preferences screen layout
    based on preferences.xml.
    This is a Fragment so you can insert it into another Activity,
    in this case SettingsActivity.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load preferences xml layout
        addPreferencesFromResource(R.xml.preferences);
    }
}
