package com.tumblr.breadcrumbs492.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class CrumbDetails extends ActionBarActivity{
    private GoogleMap map;
    private String search, name, description, username, date, tags, email, crumbID;
    private int upvotes;
    private double longitude, latitude;
    private boolean hasVoted;

    public final static int REQUEST_SETTINGS = 0;
    public final static String CRUMB_NAME = "crumbName";
    public final static String CRUMB_COMMENT = "crumbComment";
    public final static String CRUMB_TAGS = "crumbsTags";
    public final static String CRUMB_ID = "crumbID";
    public final static String CRUMB_UPVOTES = "crumbUpvotes";
    public final static String CRUMB_DATE = "crumbDate";
    public final static String CRUMB_LONGITUDE = "longitude";
    public final static String CRUMB_LATITUDE = "latitude";
    public final static String SEARCH = "search";
    public final static String USERNAME = "username";
    public final static String EMAIL = "email";
    public final static int REQUEST_FIND_CRUMB = 4;

    private TextView crumbUsername, crumbDate, crumbName, crumbUpvotes, crumbTags, crumbComment;
    private String whichActivity = "";
    private boolean fromInfoWindow = false;
    private boolean fromRankings = false;
    private boolean fromUserProfile = false;

    private MyRequestReceiver9 receiver;

    public void userDetails(View view){
        Intent intent = new Intent(CrumbDetails.this, UserProfile.class);
        intent.putExtra(EMAIL, email);
        intent.putExtra(USERNAME, username);
        intent.putExtra(CRUMB_NAME, name);
        intent.putExtra(CRUMB_COMMENT, description);
        intent.putExtra(CRUMB_UPVOTES, upvotes);
        intent.putExtra(CRUMB_DATE, date);
        intent.putExtra(CRUMB_TAGS, tags);
        intent.putExtra(CRUMB_ID, crumbID);
        intent.putExtra(CRUMB_LATITUDE, latitude);
        intent.putExtra(CRUMB_LONGITUDE, longitude);
        if(fromInfoWindow)
            intent.putExtra("activity", "infoWindowClick");
        else if(fromRankings)
            intent.putExtra("activity", "Rankings");
        else if(fromUserProfile)
            intent.putExtra("activity", "UserProfile");
        else {
            intent.putExtra(SEARCH, search);
            intent.putExtra("activity", "SearchResults");
        }
        startActivity(intent);
        finish();
    }

    public void backToResults(View view) {
        if(fromInfoWindow){
            Intent intent = new Intent(CrumbDetails.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
        else if(fromRankings){
            Intent intent = new Intent(CrumbDetails.this, Rankings.class);
            startActivity(intent);
            finish();
        }
        else if(fromUserProfile){
            Intent intent = new Intent(CrumbDetails.this, UserProfile.class);
            intent.putExtra(EMAIL, GlobalContainer.trackEmail);
            intent.putExtra(USERNAME, getIntent().getStringExtra(CrumbDetails.USERNAME));
            intent.putExtra(SEARCH, getIntent().getStringExtra(CrumbDetails.SEARCH));
            intent.putExtra(CRUMB_NAME, getIntent().getStringExtra(CrumbDetails.CRUMB_NAME));
            intent.putExtra(CRUMB_COMMENT, getIntent().getStringExtra(CrumbDetails.CRUMB_COMMENT));
            intent.putExtra(CRUMB_UPVOTES, getIntent().getIntExtra(CrumbDetails.CRUMB_UPVOTES, 0));
            intent.putExtra(CRUMB_DATE, getIntent().getStringExtra(CrumbDetails.CRUMB_DATE));
            intent.putExtra(CRUMB_TAGS, getIntent().getStringExtra(CrumbDetails.CRUMB_TAGS));
            intent.putExtra(CRUMB_ID, getIntent().getStringExtra(CrumbDetails.CRUMB_ID));
            intent.putExtra(CRUMB_LATITUDE, getIntent().getDoubleExtra(CrumbDetails.CRUMB_LATITUDE, 0.0));
            intent.putExtra(CRUMB_LONGITUDE, getIntent().getDoubleExtra(CrumbDetails.CRUMB_LONGITUDE, 0.0));
            intent.putExtra(UserProfile.ORIGINAL_EMAIL, getIntent().getStringExtra(UserProfile.ORIGINAL_EMAIL));
            startActivity(intent);
            finish();
        }
        else {
            //back to searchresults, passing same search query back to searchresults activity
            search = getIntent().getStringExtra(SearchResults.SEARCH);
            Intent intent = new Intent(CrumbDetails.this, SearchResults.class);
            intent.putExtra(SEARCH, search);
            startActivityForResult(intent, REQUEST_FIND_CRUMB);
            finish();
        }
    }

    public void voteCrumb(View view)
    {
        // change button text
        ImageButton voteButton = (ImageButton)findViewById(R.id.buttonVote);
        voteButton.setEnabled(false);
        if(hasVoted)
        {
            Intent msgIntent = new Intent(this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "unvote");
            msgIntent.putExtra("queryID", "unvote");
            msgIntent.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0] + "\",\"email\":\""
                    + GlobalContainer.user.getInfo()[1] + "\",\"crumbID\":\"" + crumbID + "\"}");

            startService(msgIntent);
            ImageButton temp_voteButton = (ImageButton) findViewById(R.id.buttonVote);
            Resources res = getResources(); /** from an Activity */
            temp_voteButton.setImageDrawable(res.getDrawable(R.drawable.newthumbup));
        }
        else
        {
            ImageButton temp_voteButton = (ImageButton) findViewById(R.id.buttonVote);
            Resources res = getResources(); /** from an Activity */
            temp_voteButton.setImageDrawable(res.getDrawable(R.drawable.newthumbdown));

            Intent msgIntent = new Intent(this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "upvote");
            msgIntent.putExtra("queryID", "upvote");
            msgIntent.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0] + "\",\"email\":\""
                    + GlobalContainer.user.getInfo()[1] + "\",\"crumbID\":\"" + crumbID + "\"}");


            startService(msgIntent);
        }

        // pass crumb back into database
        String comment = crumbComment.toString();
        Intent intent = new Intent(this, MapsActivity.class);
        String id = getIntent().getStringExtra(MyCrumbsActivity.CRUMB_ID);


        //place into intent to pass back to MapsActivity
        intent.putExtra(MapsActivity.NAME, name);
        intent.putExtra(MapsActivity.COMMENT, comment);
        setResult(RESULT_OK, intent);//send result code
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crumb_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        whichActivity = getIntent().getStringExtra("activity");
        IntentFilter filter = new IntentFilter(MyRequestReceiver9.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver9();
        registerReceiver(receiver, filter);

        if(whichActivity.equals("SearchResults")) {
            //retrieve extras from previous SearchResults class
            search = getIntent().getStringExtra(SearchResults.SEARCH);
            name = getIntent().getStringExtra(SearchResults.CRUMB_NAME);
            email = getIntent().getStringExtra(SearchResults.EMAIL);
            description = getIntent().getStringExtra(SearchResults.CRUMB_COMMENT);
            username = getIntent().getStringExtra(SearchResults.USERNAME);
            crumbID = getIntent().getStringExtra(SearchResults.CRUMB_ID);
            upvotes = getIntent().getIntExtra(SearchResults.CRUMB_UPVOTES, 0);
            date = getIntent().getStringExtra(SearchResults.CRUMB_DATE);
            tags = getIntent().getStringExtra(SearchResults.CRUMB_TAGS);
            longitude = getIntent().getDoubleExtra(SearchResults.CRUMB_LONGITUDE, 0.0);
            latitude = getIntent().getDoubleExtra(SearchResults.CRUMB_LATITUDE, 0.0);
        }

        else if(whichActivity.equals("Rankings")) {
            //retrieve extras from previous Rankings class
            fromRankings = true;
            email = getIntent().getStringExtra(Rankings.CRUMB_EMAIL);
            name = getIntent().getStringExtra(Rankings.CRUMB_NAME);
            description = getIntent().getStringExtra(Rankings.CRUMB_COMMENT);
            username = getIntent().getStringExtra(Rankings.USERNAME);
            crumbID = getIntent().getStringExtra(Rankings.CRUMB_ID);
            upvotes = getIntent().getIntExtra(Rankings.CRUMB_UPVOTES, 0);
            date = getIntent().getStringExtra(Rankings.CRUMB_DATE);
            tags = getIntent().getStringExtra(Rankings.CRUMBS_TAGS);
            longitude = getIntent().getDoubleExtra(Rankings.CRUMB_LONGITUDE, 0.0);
            latitude = getIntent().getDoubleExtra(Rankings.CRUMB_LATITUDE, 0.0);
        }
        else if(whichActivity.equals("UserProfile")) {
            //retrieve extras from previous UserProfile class
            fromUserProfile = true;
            search = getIntent().getStringExtra(UserProfile.SEARCH);
            email = getIntent().getStringExtra(UserProfile.CRUMB_EMAIL);
            name = getIntent().getStringExtra(UserProfile.CRUMB_NAME);
            description = getIntent().getStringExtra(UserProfile.CRUMB_COMMENT);
            username = getIntent().getStringExtra(UserProfile.USERNAME);
            crumbID = getIntent().getStringExtra(UserProfile.CRUMB_ID);
            upvotes = getIntent().getIntExtra(UserProfile.CRUMB_UPVOTES, 0);
            date = getIntent().getStringExtra(UserProfile.CRUMB_DATE);
            tags = getIntent().getStringExtra(UserProfile.CRUMBS_TAGS);
            longitude = getIntent().getDoubleExtra(UserProfile.CRUMB_LONGITUDE, 0.0);
            latitude = getIntent().getDoubleExtra(UserProfile.CRUMB_LATITUDE, 0.0);
        }

        else if(whichActivity.equals("infoWindowClick")){
            //retrieve extras from previous MapActivity class in info window
            fromInfoWindow = true;
            name = getIntent().getStringExtra(MapsActivity.CRUMB_NAME);
            description = getIntent().getStringExtra(MapsActivity.CRUMB_COMMENT);
            crumbID = getIntent().getStringExtra(MapsActivity.CRUMB_ID);
            username = getIntent().getStringExtra(MapsActivity.USERNAME);
            upvotes = getIntent().getIntExtra(MapsActivity.CRUMB_UPVOTES, 0);
            date = getIntent().getStringExtra(MapsActivity.CRUMB_DATE);
            tags = getIntent().getStringExtra(MapsActivity.CRUMBS_TAGS);
            email = getIntent().getStringExtra(MapsActivity.CRUMB_EMAIL);
            longitude = getIntent().getDoubleExtra(MapsActivity.CRUMB_LONGITUDE, 0.0);
            latitude = getIntent().getDoubleExtra(MapsActivity.CRUMB_LATITUDE, 0.0);
        }

        ImageButton temp_voteButton = (ImageButton) findViewById(R.id.buttonVote);
        temp_voteButton.setEnabled(false);
        Intent msgIntent1 = new Intent(this, JSONRequest.class);
        msgIntent1.putExtra(JSONRequest.IN_MSG, "hasVoted");
        msgIntent1.putExtra("queryID", "hasVoted");
        msgIntent1.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0] + "\",\"email\":\""
                + GlobalContainer.user.getInfo()[1] + "\",\"crumbID\":\"" + crumbID + "\"}");
        startService(msgIntent1);

        //setup Google map to mark where crumb is
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        map = supportMapFragment.getMap();

        //get location of crumb, set to a LatLng object
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
        String trimmedDate = date.substring(0,10);
        crumbDate.setText("Dropped on: " + trimmedDate);
        crumbUpvotes = (TextView) findViewById(R.id.upvotesTextView);
        crumbUpvotes.setText("Upvotes: " + upvotes);
        crumbUsername = (TextView) findViewById(R.id.userTextView);
        crumbUsername.setText("Dropped by: " + username);
        crumbName = (TextView) findViewById(R.id.crumb_name);
        crumbName.setText(name);

        crumbComment = (TextView) findViewById(R.id.crumb_description);
        crumbComment.setText("Comment: " + description);
        crumbTags = (TextView) findViewById(R.id.crumb_tags);
        crumbTags.setText("Tags: " + tags);

        Button backButton = (Button) findViewById(R.id.go_back);
        if(fromInfoWindow) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CrumbDetails.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        TextView clickUserName = (TextView) findViewById(R.id.userTextView);
        clickUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetails(v);

            }
        });
    }

    @Override
    public void onBackPressed() {
        backToResults(this.getCurrentFocus());
        finish();
        super.onBackPressed();
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // launch SettingsActivity with the request code. This will be referenced in
            // onActivityResult
            Intent intent = new Intent();
            intent.setClass(CrumbDetails.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }

        if(id == R.id.action_logout_crumb_details){
            GlobalContainer.user = new User();
            GlobalContainer.userIsInitialized = false;
            Intent logoutIntent = new Intent(CrumbDetails.this, LoginActivity.class);
            startActivity(logoutIntent);
            CrumbDetails.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver9 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.CrumbDetails.MyRequestReceiver";
        public String response = null;
        @Override
        public void onReceive(Context context, Intent intent) {

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            ImageButton voteButton = (ImageButton)findViewById(R.id.buttonVote);

            if(responseType.trim().equalsIgnoreCase("upvote")){

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONObject tempJSON = new JSONObject();
                try {
                    tempJSON = new JSONObject(response);
                    if(tempJSON.getString("upvoteResult").equals("true"))
                    {
                        crumbUpvotes.setText("Upvotes: " + (upvotes + 1));
                        upvotes++;
                        hasVoted = true;
                        Toast.makeText(getApplicationContext(), "Vote successful", Toast.LENGTH_SHORT).show();
                        voteButton.setEnabled(true);
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Voting error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else if(responseType.trim().equalsIgnoreCase("unvote")){
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONObject tempJSON = new JSONObject();
                try {
                    tempJSON = new JSONObject(response);
                    if(tempJSON.getString("unvoteResult").equals("true"))
                    {
                        crumbUpvotes.setText("Upvotes: " + (upvotes - 1));
                        upvotes--;
                        hasVoted = false;
                        Toast.makeText(getApplicationContext(), "Unvote successful", Toast.LENGTH_SHORT).show();
                        voteButton.setEnabled(true);
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Unvoting error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else if(responseType.trim().equalsIgnoreCase("hasVoted")){
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONObject tempJSON = new JSONObject();
                try {
                    tempJSON = new JSONObject(response);
                    ImageButton temp_voteButton = (ImageButton) findViewById(R.id.buttonVote);
                    Resources res = getResources(); /** from an Activity */
                    if(tempJSON.getString("hasVoted").equals("true"))
                    {
                        hasVoted = true;
                        temp_voteButton.setImageDrawable(res.getDrawable(R.drawable.newthumbdown));
                    }
                    else if(tempJSON.getString("hasVoted").equals("false"))
                    {
                        hasVoted = false;
                        temp_voteButton.setImageDrawable(res.getDrawable(R.drawable.newthumbup));
                    }
                    temp_voteButton.setEnabled(true);
                }
                catch(JSONException e)
                {
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
