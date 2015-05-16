package com.tumblr.breadcrumbs492.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//extending FragmentActivity disables ActionBar

public class MapsActivity extends ActionBarActivity {

    public final static int REQUEST_SETTINGS = 0;
    public final static int REQUEST_ADD_CRUMB = 1;
    public final static int REQUEST_PROFILE = 2;
    public final static int REQUEST_MYCRUMBS = 3;
    public final static int REQUEST_FIND_CRUMB = 4;
    public final static String NAME = "name";
    public final static String COMMENT = "comment";
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";
    public final static String SEARCH = "search";

    public final static String CRUMB_NAME = "crumbName";
    public final static String CRUMB_EMAIL = "email";
    public final static String CRUMB_COMMENT = "crumbComment";
    public final static String CRUMBS_TAGS = "crumbsTags";
    public final static String CRUMB_ID = "crumbID";
    public final static String CRUMB_UPVOTES = "crumbUpvotes";
    public final static String CRUMB_DATE = "crumbDate";
    public final static String CRUMB_LONGITUDE = "crumbLongitude";
    public final static String CRUMB_LATITUDE = "crumbLatitude";
    public final static String USERNAME = "username";

    private static String email;
    private static String username;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MarkerOptions markerOptions;
    private LatLng currentLocation;//store user's current location
    private static String[] tagsArr;
    private String[] usernameArr;

    public Crumb[] crumbsArr;
    public String[] idArr;
    public String[] emailArr;

    private MyRequestReceiver4 receiver;

    //button implementation for viewing user profile information
    public void viewProfile(View view) {
        //launch ProfileActivity to view user profile
        Intent intent = new Intent(this, ProfileActivity.class);

        startActivityForResult(intent, REQUEST_PROFILE);
        finish();
    }

    //button implementation for viewing user crumbs
    public void viewMyCrumbs(View view) {
        //launch MyCrumbsActivity to view user crumbs
        Intent intent = new Intent(this, MyCrumbsActivity.class);
        startActivityForResult(intent, REQUEST_MYCRUMBS);
        finish();
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

    public void randomCrumb(View view){
        Intent msgIntent = new Intent(MapsActivity.this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getRandomCrumb");
        msgIntent.putExtra("queryID", "getRandomCrumb");
        msgIntent.putExtra("jsonObject", "{\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");

        startService(msgIntent);
    }

    public void crumbRankings(View view){
        Intent intent = new Intent(this, Rankings.class);
        startActivity(intent);
        finish();

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
        Location myLocation;
        LocationManager locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if(locationManager.getLastKnownLocation(provider) == null) {
            myLocation = new Location(provider);
        }
        else {
            myLocation = locationManager.getLastKnownLocation(provider);
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();

        currentLocation = new LatLng(latitude, longitude);
    }

    //find user's current location and mark it on the map
    public void markCurrentLocation() {
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").
                //snippet("Snippet"));

        saveCurrentLocation();
        moveCameraToCurrentLocation();

        //mMap.addMarker(new MarkerOptions().position(currentLocation).
        //        title("Your are here."));
    }

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        System.out.println("create");
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        GlobalContainer.trackEmail = null;
        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver4.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver4();
        registerReceiver(receiver, filter);

        if (Session.getActiveSession().isOpened()) {

            Request.newMeRequest(Session.getActiveSession(),new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (response != null) {
                        try {
                            GlobalContainer.user = new User(user.getName(),
                                    (String) user.getProperty("email"),
                                    (String) user.getProperty("first_name"),
                                    (String) user.getProperty("last_name"),
                                    (String) user.getProperty("gender"),
                                    "city", "state");

                            GlobalContainer.userIsInitialized = true;
                            username = user.getName();
                            email = (String) user.getProperty("email");

                            Intent msgIntent = new Intent(MapsActivity.this, JSONRequest.class);
                            msgIntent.putExtra(JSONRequest.IN_MSG, "registerInit");
                            msgIntent.putExtra("queryID", "register");
                            msgIntent.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0] + "\",\"password\":\""
                                    + user.getId() + "\",\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\",\"firstName\":\""
                                    + GlobalContainer.user.getInfo()[2] +  "\",\"lastName\":\"" + GlobalContainer.user.getInfo()[3] + "\",\"gender\":\""
                                    + GlobalContainer.user.getInfo()[4] +  "\",\"city\":\"" + GlobalContainer.user.getInfo()[5] + "\",\"state\":\""
                                    + GlobalContainer.user.getInfo()[6] + "\"}");

                            startService(msgIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).executeAsync();
        }
        else{
            //non-facebook login
            //get intent received from LoginActivity
            Intent intent = getIntent();
            if(intent != null) {
                email = intent.getStringExtra("email");
                username = intent.getStringExtra("username");
                //extract boolean, false is the default value if there is none
            }
            else{
                username = GlobalContainer.user.getInfo()[0];
                email = GlobalContainer.user.getInfo()[1];
            }

            if(!GlobalContainer.userIsInitialized) {
                //initialize user object
                //populate user information fields through database
                Intent msgIntent2 = new Intent(this, JSONRequest.class);
                msgIntent2.putExtra(JSONRequest.IN_MSG, "getProfileInit");
                msgIntent2.putExtra("queryID", "getProfileInit");
                msgIntent2.putExtra("jsonObject", "{\"username\":\"" + username
                        + "\",\"email\":\"" + email + "\"}");
                startService(msgIntent2);
            }
            else
            {
                //user is initialized so get all crumbs
                Intent msgIntent = new Intent(MapsActivity.this, JSONRequest.class);
                msgIntent.putExtra(JSONRequest.IN_MSG, "getAllCrumbs");
                msgIntent.putExtra("queryID", "getAllCrumbs");
                msgIntent.putExtra("jsonObject", "{\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");

                startService(msgIntent);
            }
        }

        //Remove text from the editext_location
        Button removeTextButton = (Button) findViewById(R.id.remove_text);
        removeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextLocation = (EditText) findViewById(R.id.edittext_location);
                editTextLocation.setText("");
            }
        });

        //get button references
        ImageButton profileButton = (ImageButton) findViewById(R.id.button_1);
        ImageButton myCrumbsButton = (ImageButton) findViewById(R.id.button_2);

        //enable buttons
        profileButton.setEnabled(true);
        profileButton.setEnabled(true);

        //load settings for user login
        SettingsActivity.Settings.loadSettings(this);

        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();
                Crumb tempCrumb = new Crumb();
                String tempName = "";
                String tempComment = "";
                String tempUser = "";
                int tempRating = 0;
                for (int i = 0; i < crumbsArr.length; i++) {
                    if (latLng.toString().equals(crumbsArr[i].getLocation().toString())) {

                        tempName = crumbsArr[i].getName();
                        tempComment = crumbsArr[i].getComment();
                        tempRating = crumbsArr[i].getRating();
                        tempUser = usernameArr[i];
                    }

                }
                // Getting reference to the TextView to set latitude
                TextView tvLat = (TextView) v.findViewById(R.id.tv_crumbName);

                TextView tvRating = (TextView) v.findViewById(R.id.tv_crumbRating);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.tv_crumbComment);

                // Setting the latitude
                tvLat.setText(tempName);
                tvRating.setText("Dropped by: " + tempUser + " \nRating: " + tempRating);
                // Setting the longitude
                if (tempComment.equals(""))
                    tvLng.setText("No comment found.");
                else
                    tvLng.setText(tempComment);

                // Returning the view containing InfoWindow contents
                return v;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Getting the position from the marker
                LatLng latLng = marker.getPosition();
                String tempUser ="";
                String tempName = "";
                String tempComment = "";
                String tempDate = "";
                String tempTags = "";
                String tempSearch = "";
                String tempID = "";
                String tempEmail = "";
                int tempRating = 0;

                for (int i = 0; i < crumbsArr.length; i++) {
                    if (latLng.toString().equals(crumbsArr[i].getLocation().toString())) {
                        tempName = crumbsArr[i].getName();
                        tempComment = crumbsArr[i].getComment();
                        tempRating = crumbsArr[i].getRating();
                        tempUser = usernameArr[i];
                        tempDate = crumbsArr[i].getDate();
                        tempTags = tagsArr[i];
                        tempID = idArr[i];
                        tempEmail = emailArr[i];
                    }
                }

                Intent goToDetails = new Intent(MapsActivity.this, CrumbDetails.class);
                goToDetails.putExtra("activity", "infoWindowClick");
                goToDetails.putExtra(CRUMB_NAME,tempName);
                goToDetails.putExtra(CRUMB_COMMENT,tempComment);
                goToDetails.putExtra(CRUMB_UPVOTES,tempRating);
                goToDetails.putExtra(USERNAME,tempUser);
                goToDetails.putExtra(CRUMB_DATE, tempDate);
                goToDetails.putExtra(CRUMB_LONGITUDE, latLng.longitude);
                goToDetails.putExtra(CRUMB_LATITUDE, latLng.latitude);
                goToDetails.putExtra(CRUMBS_TAGS, tempTags);
                goToDetails.putExtra(SEARCH, tempSearch);
                goToDetails.putExtra(CRUMB_ID, tempID);
                goToDetails.putExtra(CRUMB_EMAIL, tempEmail);
                startActivity(goToDetails);
                finish();
            }
        });
    }//end onCreate

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("Stopping");
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences("facebookSave", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", GlobalContainer.user.getInfo()[1]);
        editor.putString("username", GlobalContainer.user.getInfo()[0]);
        editor.putString("firstName",  GlobalContainer.user.getInfo()[2]);
        editor.putString("lastName",  GlobalContainer.user.getInfo()[3]);
        editor.putString("gender", GlobalContainer.user.getInfo()[4]);
        editor.putString("city", GlobalContainer.user.getInfo()[5]);
        editor.putString("state", GlobalContainer.user.getInfo()[6]);
        // Commit the edits!
        editor.commit();
    }


    @Override
    protected void onDestroy() {
        System.out.println("destroying");
        unregisterReceiver(receiver);

        super.onDestroy();
    }

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

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        //will refresh the page by reloading all pins
        if(id == R.id.action_refresh) {
            //insert code to find tags
            Intent msgIntent = new Intent(MapsActivity.this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "getAllCrumbs");
            msgIntent.putExtra("queryID", "getAllCrumbs");
            msgIntent.putExtra("jsonObject", "{\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");
            startService(msgIntent);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // launch SettingsActivity with the request code. This will be referenced in
            // onActivityResult
            Intent intent = new Intent();
            intent.setClass(MapsActivity.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }

        if(id == R.id.action_logout_maps){
            GlobalContainer.user = new User();
            GlobalContainer.userIsInitialized = false;
            Intent logoutIntent = new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            MapsActivity.this.finish();
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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateNoFormat = new Date();
                String date = sdf.format(dateNoFormat);

                //create a crumb object
                Crumb crumb = new Crumb(name, comment, currentLocation, date, 0);

                //add it to map
                markCrumb(crumb);

                //move camera to new crumb
                moveCameraToCurrentLocation();

                finish();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Please enter a valid name for your crumb",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if (requestCode == REQUEST_PROFILE || requestCode == REQUEST_MYCRUMBS)
        {
            username = data.getStringExtra("username");
            email = data.getStringExtra("email");
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("Restarting");
        // Restore preferences
        SharedPreferences settings = getSharedPreferences("facebookSave", 0);
        GlobalContainer.user = new User(settings.getString("username", ""), settings.getString("email", ""),
                settings.getString("firstName", ""),settings.getString("lastName", ""),
                settings.getString("gender", ""),settings.getString("city", ""),settings.getString("state", ""));
        email = settings.getString("email", "");
        username = settings.getString("username", "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContainer.fromResume = true;
        SharedPreferences settings = getSharedPreferences("facebookSave", 0);
        GlobalContainer.user = new User(settings.getString("username", ""), settings.getString("email", ""),
                settings.getString("firstName", ""),settings.getString("lastName", ""),
                settings.getString("gender", ""),settings.getString("city", ""),settings.getString("state", ""));
        email = settings.getString("email", "");
        username = settings.getString("username", "");


        setUpMapIfNeeded();


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        //get reference to the map
        mMap = supportMapFragment.getMap();

        //user is initialized so get all crumbs
        Intent msgIntent = new Intent(MapsActivity.this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getAllCrumbs");
        msgIntent.putExtra("queryID", "getAllCrumbs");
        msgIntent.putExtra("jsonObject", "{\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");

        startService(msgIntent);

        //get reference to the find button in the xml
        Button buttonFind = (Button) findViewById(R.id.button_find);

        //define button click event listener to for the find button
        View.OnClickListener findClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get reference to edittext
                EditText editTextLocation = (EditText) findViewById(R.id.edittext_location);

                //hide keyboard after find is pressed
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(editTextLocation.getWindowToken(),0);

                //get user input for location
                String location = editTextLocation.getText().toString();

                if (location != null && !location.equals("")) {
                    //if location exists, mark it on map
                    //new GeocoderTask().execute(location);
                    //not using Geocoder class will remove the Google location searches

                    //insert code to find tags
                    Intent msgIntent = new Intent(MapsActivity.this, JSONRequest.class);
                    msgIntent.putExtra(JSONRequest.IN_MSG, "findTags");
                    msgIntent.putExtra("queryID", "findTags");
                    msgIntent.putExtra("jsonObject", "{\"tag\":\"" + location.trim() + "\"}");
                    startService(msgIntent);
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
                Toast.makeText(getBaseContext(), "No locations found.", Toast.LENGTH_SHORT).show();
            }

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

                // mMap.addMarker(markerOptions);

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

    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver4 extends BroadcastReceiver {
        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.MapsActivity.MyRequestReceiver";
        public String response = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);
            if(responseType.trim().equalsIgnoreCase("getAllCrumbs")){
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONArray tempJSON;
                try {
                    tempJSON = new JSONArray(response);
                    String name, comment;
                    LatLng location;
                    String date;
                    usernameArr = new String[tempJSON.length()];
                    emailArr = new String[tempJSON.length()];
                    tagsArr = new String[tempJSON.length()];
                    idArr = new String[tempJSON.length()];
                    int upvotes = 0;
                    crumbsArr = new Crumb[tempJSON.length()];

                    for(int i = 0; i < tempJSON.length(); i++)
                    {
                        date = tempJSON.getJSONObject(i).getString("crumbDate");
                        name = tempJSON.getJSONObject(i).getString("crumbName");
                        comment = tempJSON.getJSONObject(i).getString("comment");
                        location = new LatLng(tempJSON.getJSONObject(i).getDouble("latitude"),tempJSON.getJSONObject(i).getDouble("longitude"));
                        upvotes = tempJSON.getJSONObject(i).getInt("upvotes");
                        crumbsArr[i] = new Crumb(name, comment, location, date, upvotes);
                        usernameArr[i] = tempJSON.getJSONObject(i).getString("username");
                        emailArr[i] = tempJSON.getJSONObject(i).getString("email");
                        tagsArr[i] = tempJSON.getJSONObject(i).getString("crumbTags");
                        idArr[i] = tempJSON.getJSONObject(i).getString("crumbID");
                        markCrumb(crumbsArr[i]);
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Get crumbs failed. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            else if(responseType.trim().equalsIgnoreCase("getRandomCrumb")){
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONObject tempJSON;
                try {
                    tempJSON = new JSONObject(response);
                    LatLng location = new LatLng(tempJSON.getDouble("latitude"), tempJSON.getDouble("longitude"));

                    Crumb crumb = new Crumb(tempJSON.getString("crumbName"), tempJSON.getString("comment"), location,
                            tempJSON.getString("crumbDate"), tempJSON.getInt("upvotes"));
                    mMap.clear();
                    markCrumb(crumb);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(crumb.getLocation()));
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Retrieving random crumb failed. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            else if(responseType.trim().equalsIgnoreCase("getProfileInit")){

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONObject tempJSON;
                if(!GlobalContainer.userIsInitialized) {
                    try {
                        tempJSON = new JSONObject(response);

                        GlobalContainer.user = new User(tempJSON.getString("username"), tempJSON.getString("email"),
                                tempJSON.getString("firstName"), tempJSON.getString("lastName"), tempJSON.getString("gender"),
                                tempJSON.getString("city"), tempJSON.getString("state"));

                        GlobalContainer.userIsInitialized = true;
                        //populate user information fields through database
                        Intent msgIntent = new Intent(MapsActivity.this, JSONRequest.class);
                        msgIntent.putExtra(JSONRequest.IN_MSG, "getAllCrumbs");
                        msgIntent.putExtra("queryID", "getAllCrumbs");
                        msgIntent.putExtra("jsonObject", "{\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");

                        startService(msgIntent);
                    } catch (JSONException e) {
                        if (!GlobalContainer.fromResume) {
                            Toast.makeText(getApplicationContext(), "Retrieving user failed. Please try again.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }

            else if(responseType.trim().equalsIgnoreCase("registerInit")){
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONObject tempJSON;
                try {
                    tempJSON = new JSONObject(response);
                    //initialize user object
                    //populate user information fields through database
                    Intent msgIntent2 = new Intent(MapsActivity.this, JSONRequest.class);
                    msgIntent2.putExtra(JSONRequest.IN_MSG, "getProfileInit");
                    msgIntent2.putExtra("queryID", "getProfileInit");
                    msgIntent2.putExtra("jsonObject", "{\"username\":\"" + username
                            + "\",\"email\":\"" + email + "\"}");

                    startService(msgIntent2);
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Register user failed.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            else if(responseType.trim().equalsIgnoreCase("findTags")){
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONArray tempJSON;
                mMap.clear();
                try {
                    tempJSON = new JSONArray(response);
                    String name, comment;
                    LatLng location;
                    String date;
                    usernameArr = new String[tempJSON.length()];
                    emailArr = new String[tempJSON.length()];
                    tagsArr = new String[tempJSON.length()];
                    idArr = new String[tempJSON.length()];
                    crumbsArr = new Crumb[tempJSON.length()];

                    int upvotes = 0;
                    if (tempJSON.length() > 0) {
                        for (int i = 0; i < tempJSON.length(); i++) {
                            date = tempJSON.getJSONObject(i).getString("crumbDate");
                            name = tempJSON.getJSONObject(i).getString("crumbName");
                            comment = tempJSON.getJSONObject(i).getString("comment");
                            location = new LatLng(tempJSON.getJSONObject(i).getDouble("latitude"),tempJSON.getJSONObject(i).getDouble("longitude"));
                            upvotes = tempJSON.getJSONObject(i).getInt("upvotes");
                            crumbsArr[i] = new Crumb(name, comment, location, date, upvotes);
                            usernameArr[i] = tempJSON.getJSONObject(i).getString("username");
                            emailArr[i] = tempJSON.getJSONObject(i).getString("email");
                            tagsArr[i] = tempJSON.getJSONObject(i).getString("crumbTags");
                            idArr[i] = tempJSON.getJSONObject(i).getString("crumbID");
                            markCrumb(crumbsArr[i]);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(crumbsArr[tempJSON.length() - 1].getLocation()));                        //when search is determined to have a result, make button to display all results visible
                        Button displayAll = (Button) findViewById(R.id.button_display_all);
                        displayAll.setEnabled(true);
                        View.OnClickListener viewAllClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText editTextLocation = (EditText) findViewById(R.id.edittext_location);
                                String search = editTextLocation.getText().toString();
                                Intent intent = new Intent(MapsActivity.this, SearchResults.class);
                                intent.putExtra(SEARCH, search);
                                startActivityForResult(intent, REQUEST_FIND_CRUMB);
                                finish();
                            }
                        };
                        displayAll.setOnClickListener(viewAllClickListener);
                        Toast.makeText(getApplicationContext(), tempJSON.length() + " result(s) found.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No results found. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Get crumbs failed.", Toast.LENGTH_SHORT).show();
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
