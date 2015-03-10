package com.tumblr.breadcrumbs492.testapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends ActionBarActivity {

    private final int USER_FIELDS = 8;
    private final String SAVE_CHANGES = "Save Changes";
    private final String EDIT_PROFILE = "Edit Profile";
    private EditText[] userInfo = new EditText[USER_FIELDS];
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText gender;
    private EditText city;
    private EditText state;
    private Button editProfileButton;
    private boolean isEditingProfile;

    //button implementation for editing user profile information
    public void editProfile(View view) {
        if (isEditingProfile) {
            //set all EditText fields to be editable
            for (int i = 0; i < userInfo.length; i++) {
                userInfo[i].setEnabled(true);
                userInfo[i].setFocusable(true);
                userInfo[i].setClickable(true);
            }

            //change button text and boolean
            editProfileButton.setText(SAVE_CHANGES);
            isEditingProfile = false;
        } else {
            //set all EditText fields to be NOT editable
            for (int i = 0; i < userInfo.length; i++) {
                userInfo[i].setEnabled(false);
                userInfo[i].setFocusable(false);
                userInfo[i].setClickable(false);
            }

            //save user info in database

            //change button text and boolean
            editProfileButton.setText(EDIT_PROFILE);
            isEditingProfile = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //populate user information fields through database

        //set isEditingProfile to true
        isEditingProfile = true;

        //get all the EditText fields
        username = (EditText) findViewById(R.id.profile_username);
        password = (EditText) findViewById(R.id.profile_password);
        email = (EditText) findViewById(R.id.profile_email);
        firstName = (EditText) findViewById(R.id.profile_firstname);
        lastName = (EditText) findViewById(R.id.profile_lastname);
        gender = (EditText) findViewById(R.id.profile_gender);
        city = (EditText) findViewById(R.id.profile_city);
        state = (EditText) findViewById(R.id.profile_state);

        //place into array for easy looping later
        userInfo[0] = username;
        userInfo[1] = password;
        userInfo[2] = email;
        userInfo[3] = firstName;
        userInfo[4] = lastName;
        userInfo[5] = gender;
        userInfo[6] = city;
        userInfo[7] = state;

        //get Button reference
        editProfileButton = (Button) findViewById(R.id.profile_editprofile);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
