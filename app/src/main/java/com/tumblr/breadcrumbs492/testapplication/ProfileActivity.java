package com.tumblr.breadcrumbs492.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {
    private final int USER_FIELDS = 8;
    private final String SAVE_CHANGES = "Saving Changes";
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
    private boolean isEditingProfile = false;
    private MyRequestReceiver1 receiver;

    //button implementation for editing user profile information
    public void editProfile(View view) {
        if (isEditingProfile) {
            //set all EditText fields to be editable
            //setting i to 1 to skip allowing username to be editable
            for (int i = 1; i < userInfo.length; i++) {
                userInfo[i].setEnabled(true);
                userInfo[i].setFocusable(true);
                userInfo[i].setClickable(true);
            }

            GlobalContainer.userIsInitialized = false;
            Intent msgIntent = new Intent(this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "updateProfile");
            msgIntent.putExtra("queryID", "updateProfile");
            msgIntent.putExtra("jsonObject", "{\"username\":\"" + username.getText().toString() + "\",\"oldUsername\":\"" + GlobalContainer.user.getInfo()[0]
                    + "\",\"oldEmail\":\"" + GlobalContainer.user.getInfo()[1] + "\",\"password\":\""
                    + password.getText().toString() + "\",\"email\":\"" + email.getText().toString()
                    + "\",\"firstName\":\"" + firstName.getText().toString() + "\",\"lastName\":\"" + lastName.getText().toString()
                    + "\",\"gender\":\"" + gender.getText().toString().charAt(0) + "\",\"city\":\"" + city.getText().toString()
                    +  "\",\"state\":\"" + state.getText().toString() + "\"}");


            startService(msgIntent);

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

            //change button text and boolean
            editProfileButton.setText(EDIT_PROFILE);
            isEditingProfile = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver1.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver1();
        registerReceiver(receiver, filter);

        //populate user information fields through database

        Intent intent = getIntent();
        Intent msgIntent = new Intent(this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getProfile");
        msgIntent.putExtra("queryID", "getProfile");
        msgIntent.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0]
                            + "\",\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");

        startService(msgIntent);

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
        userInfo[0] = password;
        userInfo[1] = username;
        userInfo[2] = email;
        userInfo[3] = firstName;
        userInfo[4] = lastName;
        userInfo[5] = gender;
        userInfo[6] = city;
        userInfo[7] = state;

        for(int i = 1; i < userInfo.length; i++)
        {
            userInfo[i].setText(GlobalContainer.user.getInfo()[i-1]);
        }
        //get Button reference
        editProfileButton = (Button) findViewById(R.id.profile_editprofile);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
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
            //launch settings screen
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if(id == R.id.action_logout_profile){
            GlobalContainer.user = new User();
            GlobalContainer.userIsInitialized = false;
            Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            ProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver1 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.ProfileActivity.MyRequestReceiver";
        public String response = null;
        @Override
        public void onReceive(Context context, Intent intent) {

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            if(responseType.trim().equalsIgnoreCase("getProfile")){

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONObject tempJSON = new JSONObject();
                try {
                    tempJSON = new JSONObject(response);
                    password.setText(tempJSON.getString("password"));
                    city.setText(tempJSON.getString("city"));
                    state.setText(tempJSON.getString("state"));
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Profile retrieval failed. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else if(responseType.trim().equalsIgnoreCase("updateProfile"))
            {
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONObject tempJSON = new JSONObject();

                try {
                    tempJSON = new JSONObject(response);

                    if (tempJSON.getString("updateProfileResult").equals("true")) {

                        Intent intent2 = new Intent(ProfileActivity.this, MapsActivity.class);
                        intent2.putExtra("username",tempJSON.getString("username"));
                        intent2.putExtra("email",tempJSON.getString("email"));
                        startActivity(intent2);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Edit profile failed. Please try again.", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(ProfileActivity.this, MapsActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Edit profile failed. Please try again.", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(ProfileActivity.this, MapsActivity.class);
                    startActivity(intent2);
                    finish();
                    e.printStackTrace();
                }
            }
        }
        public String getResponse()
        {
            return response;
        }
    }
}
