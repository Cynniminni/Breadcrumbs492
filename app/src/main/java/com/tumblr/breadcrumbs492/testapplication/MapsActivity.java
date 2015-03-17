package com.tumblr.breadcrumbs492.testapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;

//extending FragmentActivity disables ActionBar

public class MapsActivity extends ActionBarActivity {

    public final static int REQUEST_SETTINGS = 0;
    public final static int REQUEST_ADD_CRUMB = 1;
    public final static String NAME = "name";
    public final static String COMMENT = "comment";
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";
    public final static String GUESTLOGIN = "guest login";

    private static String username;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MarkerOptions markerOptions;
    private LatLng currentLocation;//store user's current location
    private boolean isGuestLogin;

    //button implementation for viewing user profile information
    public void viewProfile(View view) {
        //launch ProfileActivity to view user profile
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //button implementation for viewing user crumbs
    public void viewMyCrumbs(View view) {
        //launch MyCrumbsActivity to view user crumbs
        Intent intent = new Intent(this, MyCrumbsActivity.class);
        startActivity(intent);
    }

    //button implementation for adding crumbs to the map
    public void addCrumbs(View view) {
        Intent intent = new Intent(this, AddCrumbActivity.class);
        //get user's current location
        double latitude = currentLocation.latitude;
        double longitude = currentLocation.longitude;
        //pass into intent
        intent.putExtra(LATITUDE, latitude);
        intent.putExtra(LONGITUDE, longitude);
        //pass intent to AddCrumbActivity with the request code
        startActivityForResult(intent, REQUEST_ADD_CRUMB);
    }

    //add a crumb marker on the map
    public void markCrumb(Crumb crumb) {
        //add marker with location, name, and comment
        mMap.addMarker(new MarkerOptions().position(crumb.getLocation())
                .title(crumb.getName())
                .snippet(crumb.getComment()));
    }

    //move camera to current location
    public void moveCameraToCurrentLocation() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        LatLng myCoordinates = currentLocation;
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 12);
        mMap.animateCamera(yourLocation);
    }

    public void saveCurrentLocation() {
        // Enable MyLocation Layer of Google Map
        // This "continuously draws an indication of a user's current location and bearing, and
        // displays UI controls that allow a user to interact with their location"
        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();

        currentLocation = new LatLng(latitude, longitude);
    }

    //find user's current location and mark it on the map
    public void markCurrentLocation() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").
                snippet("Snippet"));

        saveCurrentLocation();
        moveCameraToCurrentLocation();

        //mMap.addMarker(new MarkerOptions().position(currentLocation).
        //        title("Your are here."));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        //get intent received from LoginActivity
        Intent intent = getIntent();

        //extract boolean, false is the default value if there is none
        isGuestLogin = intent.getBooleanExtra(GUESTLOGIN, false);

        username = intent.getStringExtra("username");

        //get button references
        Button profileButton = (Button) findViewById(R.id.button_1);
        Button myCrumbsButton = (Button) findViewById(R.id.button_2);

        if (isGuestLogin) {
            //disable buttons
            profileButton.setEnabled(false);
            myCrumbsButton.setEnabled(false);
        } else {
            //enable buttons
            profileButton.setEnabled(true);
            profileButton.setEnabled(true);

            //load settings for user login
            SettingsActivity.Settings.loadSettings(this);
        }
    }//end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
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
            // launch SettingsActivity with the request code. This will be referenced in
            // onActivityResult
            Intent intent = new Intent();
            intent.setClass(MapsActivity.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SETTINGS) {
            //Reload settings throughout MapActivity every time user returns from SettingsActivity
            SettingsActivity.Settings.loadSettings(this);
        } else if (requestCode == REQUEST_ADD_CRUMB) {
            if (resultCode == RESULT_OK) {
                //extract name and comment passed from AddCrumbActivity
                String name = data.getStringExtra(NAME);
                String comment = data.getStringExtra(COMMENT);

                //create a crumb object
                Crumb crumb = new Crumb(name, comment, currentLocation, new Date());

                //add it to map
                markCrumb(crumb);

                //move camera to new crumb
                moveCameraToCurrentLocation();

                //show output
                //later this will add a crumb
                Toast.makeText(getApplicationContext(),
                        "Name = " + name + "Comment = " + comment, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Please enter a valid name for your crumb",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        //get reference to the map
        mMap = supportMapFragment.getMap();

        //get reference to the find button in the xml
        Button buttonFind = (Button) findViewById(R.id.button_find);

        //define button click event listener to for the find button
        View.OnClickListener findClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get reference to edittext
                EditText editTextLocation = (EditText) findViewById(R.id.edittext_location);

                //get user input for location
                String location = editTextLocation.getText().toString();

                if (location != null && !location.equals("")) {
                    //if location exists, mark it on map
                    new GeocoderTask().execute(location);
                }
            }
        };

        //set the button click listener to the find button
        buttonFind.setOnClickListener(findClickListener);
    }


    /*
        Private class for searching addresses on the map.
     */
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(String... locationName) {
            //create instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                //get three instances of addresses that match input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location Found", Toast.LENGTH_SHORT).show();
            }

            //clear all existing markers on the map
            //mMap.clear();

            //add markers for all matching addresses
            for (int i = 0; i < addresses.size(); i++) {
                Address address = (Address) addresses.get(i);

                //create instance of geopoint, to display in google map
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                mMap.addMarker(markerOptions);

                //locate the first location
                if (i == 0) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }//end if
            }//end for
        }//end onPostExecute
    }//end private class

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        markCurrentLocation();
    }
}
