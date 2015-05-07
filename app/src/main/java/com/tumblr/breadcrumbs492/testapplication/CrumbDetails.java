package com.tumblr.breadcrumbs492.testapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;


import org.w3c.dom.Text;

import java.util.Date;


public class CrumbDetails extends ActionBarActivity /*implements OnStreetViewPanoramaReadyCallback*/{
    //private StreetViewPanoramaFragment panorama;
    private GoogleMap map;
    private String search, name, description, username, date, tags;
    private int upvotes;
    private double longitude, latitude;

    public final static String CRUMB_NAME = "crumbName";
    public final static String CRUMB_COMMENT = "crumbComment";
    public final static String CRUMB_TAGS = "crumbsTags";
    public final static String CRUMB_ID = "crumbID";
    public final static String CRUMB_UPVOTES = "crumbUpvotes";
    public final static String CRUMB_DATE = "crumbDate";
    public final static String CRUMB_LONGITUDE = "crumbLongitude";
    public final static String CRUMB_LATITUDE = "crumbLatitude";
    public final static String SEARCH = "search";
    public final static String USERNAME = "username";
    public final static String EMAIL = "email";
    public final static int REQUEST_FIND_CRUMB = 4;

    private TextView crumbUsername, crumbDate, crumbName, crumbUpvotes, crumbTags, crumbComment;
    private String whichActivity = "";

    public void userDetails(View view){
        Intent intent = new Intent(CrumbDetails.this, UserProfile.class);
        intent.putExtra(EMAIL, getIntent().getStringExtra(SearchResults.EMAIL));
        intent.putExtra(USERNAME, getIntent().getStringExtra(SearchResults.USERNAME));
        intent.putExtra(SEARCH, getIntent().getStringExtra(SearchResults.SEARCH));
        intent.putExtra(CRUMB_NAME, getIntent().getStringExtra(SearchResults.CRUMB_NAME));
        intent.putExtra(CRUMB_COMMENT, getIntent().getStringExtra(SearchResults.CRUMB_COMMENT));
        intent.putExtra(CRUMB_UPVOTES, getIntent().getIntExtra(SearchResults.CRUMB_UPVOTES, 0));
        intent.putExtra(CRUMB_DATE, getIntent().getStringExtra(SearchResults.CRUMB_DATE));
        intent.putExtra(CRUMB_TAGS, getIntent().getStringExtra(SearchResults.CRUMB_TAGS));
        intent.putExtra(CRUMB_LATITUDE, getIntent().getDoubleExtra(SearchResults.CRUMB_LATITUDE, 0.0));
        intent.putExtra(CRUMB_LONGITUDE, getIntent().getDoubleExtra(SearchResults.CRUMB_LONGITUDE, 0.0));
        startActivity(intent);
        finish();

    }

    public void backToResults(View view) {
        //back to searchresults, passing same search query back to searchresults activity
        search = getIntent().getStringExtra(SearchResults.SEARCH);
        Intent intent = new Intent(CrumbDetails.this, SearchResults.class);
        intent.putExtra(SEARCH, search);
        startActivityForResult(intent, REQUEST_FIND_CRUMB);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crumb_details);
        whichActivity = getIntent().getStringExtra("activity");

        if(whichActivity.equals("SearchResults")) {
            //retrieve extras from previous SearchResults class
            search = getIntent().getStringExtra(SearchResults.SEARCH);
            name = getIntent().getStringExtra(SearchResults.CRUMB_NAME);
            description = getIntent().getStringExtra(SearchResults.CRUMB_COMMENT);
            username = getIntent().getStringExtra(SearchResults.USERNAME);
            upvotes = getIntent().getIntExtra(SearchResults.CRUMB_UPVOTES, 0);
            date = getIntent().getStringExtra(SearchResults.CRUMB_DATE);
            tags = getIntent().getStringExtra(SearchResults.CRUMB_TAGS);
            longitude = getIntent().getDoubleExtra(SearchResults.CRUMB_LONGITUDE, 0.0);
            latitude = getIntent().getDoubleExtra(SearchResults.CRUMB_LATITUDE, 0.0);
            System.out.println("Tags: " + tags);
        }

        else if(whichActivity.equals("MapActivity")) {
            //retrieve extras from previous SearchResults class
            search = getIntent().getStringExtra(MapsActivity.SEARCH);
            name = getIntent().getStringExtra(MapsActivity.CRUMB_NAME);
            description = getIntent().getStringExtra(MapsActivity.CRUMB_COMMENT);
            username = getIntent().getStringExtra(MapsActivity.USERNAME);
            upvotes = getIntent().getIntExtra(MapsActivity.CRUMB_UPVOTES, 0);
            date = getIntent().getStringExtra(MapsActivity.CRUMB_DATE);
            tags = getIntent().getStringExtra(MapsActivity.CRUMB_TAGS);
            longitude = getIntent().getDoubleExtra(MapsActivity.CRUMB_LONGITUDE, 0.0);
            latitude = getIntent().getDoubleExtra(MapsActivity.CRUMB_LATITUDE, 0.0);
            System.out.println("Tags: " + tags);
        }
        else if(whichActivity.equals("UserProfile")) {
            //retrieve extras from previous SearchResults class
            search = getIntent().getStringExtra(UserProfile.SEARCH);
            name = getIntent().getStringExtra(UserProfile.CRUMB_NAME);
            description = getIntent().getStringExtra(UserProfile.CRUMB_COMMENT);
            username = getIntent().getStringExtra(UserProfile.USERNAME);
            upvotes = getIntent().getIntExtra(UserProfile.CRUMB_UPVOTES, 0);
            date = getIntent().getStringExtra(UserProfile.CRUMB_DATE);
            tags = getIntent().getStringExtra(UserProfile.CRUMB_TAGS);
            longitude = getIntent().getDoubleExtra(UserProfile.CRUMB_LONGITUDE, 0.0);
            latitude = getIntent().getDoubleExtra(UserProfile.CRUMB_LATITUDE, 0.0);
            System.out.println("Tags: " + tags);
        }

        /*StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.panorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);*/


        //setup Google map to mark where crumb is
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        map = supportMapFragment.getMap();

        //get location of crumb, set to a LatLng object
        System.out.println("Latitude: " + latitude + "Longitude: " + longitude);
        LatLng location = new LatLng(latitude, longitude);

        //mark crumb location on map
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
        map.addMarker(new MarkerOptions().position(location).
                title(name));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 12);
        map.animateCamera(yourLocation);

        //get reference to TextViews and populate them with extras from variables above
        crumbDate = (TextView) findViewById(R.id.dateTextView);
        crumbDate.setText("Crumb dropped on: " + date);
        crumbUpvotes = (TextView) findViewById(R.id.upvotesTextView);
        crumbUpvotes.setText("Upvotes: " + upvotes);
        //crumbUpvotes.setText("Upvotes: " + getIntent().getIntExtra(SearchResults.CRUMB_UPVOTES, 1));
        crumbUsername = (TextView) findViewById(R.id.userTextView);
        crumbUsername.setText("Crumb dropped by: " + username);
        crumbName = (TextView) findViewById(R.id.crumb_name);
        crumbName.setText("Name: " + name);
        crumbComment = (TextView) findViewById(R.id.crumb_description);
        /*if (description == "") {
            crumbComment.setTypeface(null, Typeface.ITALIC);
            description = "No description found for this crumb.";
            crumbComment.setText("Description: " + description);
        }*/
        crumbComment.setText("Description: " + description);
        crumbTags = (TextView) findViewById(R.id.crumb_tags);
        /*if (tags == "") {
            crumbTags.setTypeface(null, Typeface.ITALIC);
            tags = "No tags found for this crumb.";
            crumbTags.setText(tags);
        }*/

        crumbTags.setText("Tags: " + tags);
    }

   /* @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        System.out.println(latitude + " " + longitude);
        panorama.setPosition(new LatLng(latitude, longitude));
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MapsActivity.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crumb_details, menu);
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
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
