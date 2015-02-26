package com.tumblr.breadcrumbs492.testapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    //sign in or register button click
    public void signInOrRegister(View view) {
        //get text from text fields
        EditText username = (EditText) findViewById(R.id.enter_username);
        EditText password = (EditText) findViewById(R.id.enter_password);

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        if (usernameText.equalsIgnoreCase("admin") && passwordText.equals("admin")) {
            //this is the default user login that will launch the map activity
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (usernameText.equals("") && passwordText.equals("")) {
            //if fields are empty then launch register activity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else {
            //this is for all user logins
            //credentials will be checked against a database for verification here
            //if successful, it will launch the map activity
            //toast is a UI notification the user will see
            Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
        }//end if else
    }//end signInOrRegister

    public void loginAsGuest(View view) {
        //automatically launch MapsActivity when signing in as guest
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
