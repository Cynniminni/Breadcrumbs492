package com.tumblr.breadcrumbs492.testapplication;

import android.content.Intent;
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


public class AddCrumbActivity extends ActionBarActivity {

    private GoogleMap map;
    private EditText crumbName;
    private EditText crumbComment;
    private double latitude;
    private double longitude;

    //implement adding a crumb to the map
    public void addCrumb(View view) {
        //extract String input from user
        String name = crumbName.getText().toString();
        String comment = crumbComment.getText().toString();

        Intent intent = getIntent();

        if (name.equals("")) {
            //if user entered nothing then cancel adding a crumb
            setResult(RESULT_CANCELED, intent);
        } else {
            //place into intent to pass back to MapsActivity
            intent.putExtra(MapsActivity.NAME, name);
            intent.putExtra(MapsActivity.COMMENT, comment);
            setResult(RESULT_OK, intent);//send result code
        }

        finish();//close this activity and return to MapsActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crumb);

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
}
