package com.tumblr.breadcrumbs492.testapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity {

    private EditText[] userInfo;
    private String[] userInfoStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onCancel(View view) {
        //go back to parent activity, which is the login screen
        NavUtils.navigateUpFromSameTask(this);
    }

    public void onRegister(View view) {
        //get all EditText references
        userInfo = new EditText[8];
        userInfo[0] = (EditText) findViewById(R.id.et_username);
        userInfo[1] = (EditText) findViewById(R.id.et_password);
        userInfo[2] = (EditText) findViewById(R.id.et_email);
        userInfo[3] = (EditText) findViewById(R.id.et_first_name);
        userInfo[4] = (EditText) findViewById(R.id.et_last_name);
        userInfo[5] = (EditText) findViewById(R.id.et_gender);
        userInfo[6] = (EditText) findViewById(R.id.et_city);
        userInfo[7] = (EditText) findViewById(R.id.et_state);

        boolean allFieldsFilled = true;

        //extract strings from EditText
        userInfoStrings = new String[8];
        for (int i = 0; i < userInfoStrings.length; i++) {
            userInfoStrings[i] = userInfo[i].getText().toString();

            if (userInfoStrings[i].isEmpty()) {
                //if one any of the fields are blank, change its field to red
                userInfo[i].setHintTextColor(Color.RED);

                //set boolean flag for checking later
                allFieldsFilled = false;
            }
        }//end for

        if (allFieldsFilled) {
            //if all fields are filled, verify against database

            //insert user info into database if verified

            //launch MapsActivity as new user

            //log in as new user and start the map activity
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else {
            //notify user
            Toast.makeText(getApplicationContext(), "Please fill in all fields to continue.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
}
