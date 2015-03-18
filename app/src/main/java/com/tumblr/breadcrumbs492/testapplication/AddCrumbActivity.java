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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class AddCrumbActivity extends ActionBarActivity {

    private GoogleMap map;
    private EditText crumbName;
    private EditText crumbComment;
    private double latitude;
    private double longitude;
    private String uName;
    private MyRequestReceiver2 receiver;
    private String name;
    private String comment;
    private String addResult;
<<<<<<< HEAD

    //implement adding a crumb to the map
    public void addCrumb(View view) {
        //extract String input from user
        name = crumbName.getText().toString();
        comment = crumbComment.getText().toString();

        //get username
        Intent intent = getIntent();
        uName = intent.getStringExtra("username");

        if (name.equals("")) {
            //if user entered nothing then cancel adding a crumb
            setResult(RESULT_CANCELED, intent);
        } else {
            JSONObject jObject = new JSONObject();
            Intent msgIntent = new Intent(this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "addCrumb");
            msgIntent.putExtra("queryID", "addCrumb");
            msgIntent.putExtra("jsonObject", "{\"username\":\"" + uName + "\",\"name\":\"" + name
                    + "\",\"comment\":\"" + comment + "\",\"latitude\":\""
                    + latitude + "\",\"longitude\":\"" + longitude  + "\"}");
            msgIntent.putExtra("intent", intent.toUri(Intent.URI_INTENT_SCHEME));
            startService(msgIntent);

            //place into intent to pass back to MapsActivity
            intent.putExtra(MapsActivity.NAME, name);
            intent.putExtra(MapsActivity.COMMENT, comment);
            setResult(RESULT_OK, intent);//send result code
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crumb);

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver2.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver2();
        registerReceiver(receiver, filter);

        //get references to the EditText fields
        crumbName = (EditText) findViewById(R.id.addcrumb_name);
        crumbComment = (EditText) findViewById(R.id.addcrumb_comment);

        //get reference to the map
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        map = supportMapFragment.getMap();

        //get user location
        latitude = getIntent().getDoubleExtra(MapsActivity.LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(MapsActivity.LONGITUDE, 0);
        LatLng location = new LatLng(latitude, longitude);

        //mark user location on map
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
        map.addMarker(new MarkerOptions().position(location).
                title("Your are here."));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 12);
        map.animateCamera(yourLocation);





    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_crumb, menu);
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
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver2 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity.MyRequestReceiver";
        public String response = null;
        @Override
        public void onReceive(Context context, Intent intent) {
<<<<<<< HEAD

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);
            Intent addCrumbIntent = new Intent(AddCrumbActivity.this, MapsActivity.class);
            if(responseType.trim().equalsIgnoreCase("addCrumb")){

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONObject tempJSON = new JSONObject();



                try {
                    tempJSON = new JSONObject(response);
                    if(tempJSON.getString("addCrumbResult").trim().equals("true"))
                    {
                        Toast.makeText(getApplicationContext(), "successfully added crumb", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "add crumb failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


<<<<<<< HEAD
                addCrumbIntent.putExtra("username", uName);
                startActivity(addCrumbIntent);//close this activity and return to MapsActivity
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
