package com.tumblr.breadcrumbs492.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends ActionBarActivity {
    private EditText[] userInfo;
    private String[] userInfoStrings;
    private MyRequestReceiver5 receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver5.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver5();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
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
            Intent msgIntent = new Intent(this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "register");
            msgIntent.putExtra("queryID", "register");
            msgIntent.putExtra("jsonObject", "{\"username\":\"" + userInfoStrings[0] + "\",\"password\":\""
                    + userInfoStrings[1] + "\",\"email\":\"" + userInfoStrings[2] + "\",\"firstName\":\""
                    + userInfoStrings[3] +  "\",\"lastName\":\"" + userInfoStrings[4] + "\",\"gender\":\""
                    + userInfoStrings[5] +  "\",\"city\":\"" + userInfoStrings[6] + "\",\"state\":\""
                    + userInfoStrings[7] + "\"}");

            startService(msgIntent);

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
    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver5 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.RegisterActivity.MyRequestReceiver";
        public String response = null;
        @Override
        public void onReceive(Context context, Intent intent) {

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            if(responseType.trim().equalsIgnoreCase("register")){

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONObject tempJSON = new JSONObject();
                try {
                    tempJSON = new JSONObject(response);
                    if(tempJSON.getString("registerResult").equals("true"))
                    {
                        //log in as new user and start the map activity
                        Intent intent1 = new Intent(RegisterActivity.this, MapsActivity.class);
                        intent1.putExtra("email", userInfoStrings[2]);
                        intent1.putExtra("username", userInfoStrings[0]);
                        startActivity(intent1);
                        finish();
                    }
                    else if(tempJSON.getString("registerResult").equals("false")) {
                        Toast.makeText(getApplicationContext(), "Invalid username or email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Registration error. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else{
                //you can choose to implement another transaction here
            }
        }
        public String getResponse()
        {
            return response;
        }
    }
}
