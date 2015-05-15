package com.tumblr.breadcrumbs492.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONArray;


public class UserProfile extends ActionBarActivity {
    private MyRequestReceiver8 receiver;
    private String search, email;

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
    public final static String ORIGINAL_EMAIL = "originalEmail";
    public final static String SEARCH = "search";
    public final static int REQUEST_SETTINGS = 0;

    int arr[] = new int[5];
    int arr2[] = new int[15];
    private ListView listView;
    //Arrays to be used for User's Crumbs
    String[] username;
    String[] names;
    String[] ids;
    String[] comments;
    String[] tags;
    String[] dates;
    String[] emails;
    Integer[] rank;
    Integer[] upvotes;
    Double[] longitude;
    Double[] latitude;
    //Arrays to be used for User's liked crumbs
    String[] usernameForLikes;
    String[] namesForLikes;
    String[] idsForLikes;
    String[] commentsForLikes;
    String[] tagsForLikes;
    String[] datesForLikes;
    String[] emailsForLikes;
    Integer[] rankForLikes;
    Integer[] upvotesForLikes;
    Double[] longitudeForLikes;
    Double[] latitudeForLikes;
    public int numOfCrumbsDropped;
    public boolean showMyCrumbs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver8.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver8();
        registerReceiver(receiver, filter);

        String userFromIntent = getIntent().getStringExtra(CrumbDetails.USERNAME);
        TextView tempUserName = (TextView) findViewById(R.id.tv_username);
        tempUserName.setText(userFromIntent);



        if(!(getIntent().getStringExtra(CrumbDetails.SEARCH) == null))
            search = getIntent().getStringExtra(CrumbDetails.SEARCH);

        email = getIntent().getStringExtra(CrumbDetails.EMAIL);
        if(GlobalContainer.trackEmail == null)
            GlobalContainer.trackEmail = email;
        Log.d(email, "is username");
        Intent msgIntent = new Intent(this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getUserCrumbs");
        msgIntent.putExtra("queryID", "getUserCrumbs");
        msgIntent.putExtra("jsonObject", "{\"email\":\"" + email + "\"}");

        startService(msgIntent);

        //get ListView reference from xml
        listView = (ListView) findViewById(R.id.listView);



        final ToggleButton displaySwitch = (ToggleButton) findViewById(R.id.toggleDisplay);
        displaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!displaySwitch.isChecked()) {
                    //Display the user's favorite crumbs

                    CustomListAdapter2 adapter = new CustomListAdapter2(UserProfile.this, names, dates, rank, upvotes, username);
                    listView.setAdapter(adapter);

                    //add ListView item click listener to interact with each item
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //get position of item clicked
                            int itemPosition = position;

                            //get value of item clicked
                            String itemValue = (String) listView.getItemAtPosition(position);

                            //output item

                            //position that was clicked determines array element to retrieve
                            //attributes of crumb selected and passed to EditCrumb activity
                            Intent intent = new Intent(UserProfile.this, CrumbDetails.class);
                            intent.putExtra(CRUMB_NAME, names[itemPosition]);
                            intent.putExtra(CRUMB_ID, ids[itemPosition]);
                            intent.putExtra(CRUMB_COMMENT, comments[itemPosition]);
                            intent.putExtra(CRUMBS_TAGS, tags[itemPosition]);
                            intent.putExtra(CRUMB_UPVOTES, upvotes[itemPosition]);
                            intent.putExtra(CRUMB_DATE, dates[itemPosition]);
                            intent.putExtra(SEARCH, getIntent().getStringExtra(CrumbDetails.SEARCH));
                            intent.putExtra(USERNAME, username[itemPosition]);
                            intent.putExtra(CRUMB_LONGITUDE, longitude[itemPosition]);
                            intent.putExtra(CRUMB_LATITUDE, latitude[itemPosition]);
                            intent.putExtra(CRUMB_EMAIL, emails[itemPosition]);
                            intent.putExtra(ORIGINAL_EMAIL, email);

                            if (getIntent().getStringExtra("activity").equals("infoWindowClick")) {
                                intent.putExtra("activity", "infoWindowClick");
                            } else if (getIntent().getStringExtra("activity").equals("Rankings")) {
                                intent.putExtra("activity", "Rankings");
                            } else {
                                intent.putExtra("activity", "UserProfile");
                            }

                            startActivity(intent);
                            finish();
                        }
                    });
                    //END OF IF
                } else if (displaySwitch.isChecked()) {
                    //Default behavior, display the user's own crumbs
                    //       listView
                    CustomListAdapter2 adapter2 = new CustomListAdapter2(UserProfile.this, namesForLikes, datesForLikes, rankForLikes, upvotesForLikes, usernameForLikes);
                    listView.setAdapter(adapter2);

                    //add ListView item click listener to interact with each item
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //get position of item clicked
                            int itemPosition = position;

                            //get value of item clicked
                            String itemValue = (String) listView.getItemAtPosition(position);


                            //position that was clicked determines array element to retrieve
                            //attributes of crumb selected and passed to EditCrumb activity
                            Intent intent = new Intent(UserProfile.this, CrumbDetails.class);
                            intent.putExtra(CRUMB_NAME, namesForLikes[itemPosition]);
                            intent.putExtra(CRUMB_ID, idsForLikes[itemPosition]);
                            intent.putExtra(CRUMB_COMMENT, commentsForLikes[itemPosition]);
                            intent.putExtra(CRUMBS_TAGS, tagsForLikes[itemPosition]);
                            intent.putExtra(CRUMB_UPVOTES, upvotesForLikes[itemPosition]);
                            intent.putExtra(CRUMB_DATE, datesForLikes[itemPosition]);
                            intent.putExtra(SEARCH, getIntent().getStringExtra(CrumbDetails.SEARCH));

                            intent.putExtra(USERNAME, usernameForLikes[itemPosition]);
                            intent.putExtra(CRUMB_LONGITUDE, longitudeForLikes[itemPosition]);
                            intent.putExtra(CRUMB_LATITUDE, latitudeForLikes[itemPosition]);
                            intent.putExtra(CRUMB_EMAIL, emailsForLikes[itemPosition]);
                            intent.putExtra(ORIGINAL_EMAIL, email);

                            if (getIntent().getStringExtra("activity").equals("infoWindowClick")) {
                                intent.putExtra("activity", "infoWindowClick");
                            } else if (getIntent().getStringExtra("activity").equals("Rankings")) {
                                intent.putExtra("activity", "Rankings");
                            } else if (getIntent().getStringExtra("activity").equals("SearchResults")) {
                                intent.putExtra("activity", "SearchResults");
                            } else {
                                intent.putExtra("activity", "UserProfile");
                            }
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if(getIntent().getStringExtra("activity") != null) {

            if (getIntent().getStringExtra("activity").equals("Rankings")) {
                intent = new Intent(this, Rankings.class);
            } else if (getIntent().getStringExtra("activity").equals("SearchResults")) {
                intent = new Intent(this, SearchResults.class);
                intent.putExtra(SEARCH, search);
            }
            else intent = new Intent(this, MapsActivity.class);
        }
        else
            intent = new Intent(this, MapsActivity.class);

        startActivity(intent);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // launch SettingsActivity with the request code. This will be referenced in
            // onActivityResult
            Intent intent = new Intent();
            intent.setClass(UserProfile.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }

        if(id == R.id.action_logout_user_profile){
            GlobalContainer.user = new User();
            GlobalContainer.userIsInitialized = false;
            Intent logoutIntent = new Intent(UserProfile.this, LoginActivity.class);
            startActivity(logoutIntent);
            UserProfile.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver8 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.UserProfile.MyRequestReceiver";
        public String response = null;

        @Override
        public void onReceive(Context context, Intent intent) {

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            if (responseType.trim().equalsIgnoreCase("getUserCrumbs")) {

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONArray tempJSON = new JSONArray();
                try {
                    tempJSON = new JSONArray(response);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Get crumbs for this user failed.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                //array declarations that store all of the users' crumbs' attributes
                username = new String[tempJSON.length()];
                names = new String[tempJSON.length()];
                ids = new String[tempJSON.length()];
                comments = new String[tempJSON.length()];
                tags = new String[tempJSON.length()];
                dates = new String[tempJSON.length()];
                emails = new String[tempJSON.length()];
                rank = new Integer[tempJSON.length()];
                upvotes = new Integer[tempJSON.length()];
                longitude = new Double[tempJSON.length()];
                latitude = new Double[tempJSON.length()];

                try {
                    //populate the arrays with each crumbs' attributes
                    int upvoteCounter = 0;
                    int counter = 0;
                    for (int i = 0; i < tempJSON.length(); i++) {
                        username[i] = tempJSON.getJSONObject(i).getString("username");
                        names[i] = tempJSON.getJSONObject(i).getString("crumbName");
                        ids[i] = tempJSON.getJSONObject(i).getString("crumbID");
                        comments[i] = tempJSON.getJSONObject(i).getString("comment");
                        upvotes[i] = tempJSON.getJSONObject(i).getInt("upvotes");
                        longitude[i] = tempJSON.getJSONObject(i).getDouble("longitude");
                        latitude[i] = tempJSON.getJSONObject(i).getDouble("latitude");
                        dates[i] = tempJSON.getJSONObject(i).getString("crumbDate");
                        tags[i] = tempJSON.getJSONObject(i).getString("crumbTags");
                        emails[i] = tempJSON.getJSONObject(i).getString("email");
                        rank[i] = i + 1;
                        counter++;
                        upvoteCounter = upvoteCounter + upvotes[i];
                    }

                    TextView numDropped = (TextView) findViewById(R.id.tv_numOfCrumbs);
                    numDropped.setText("Number of crumbs dropped: " + counter);

                    Intent likeCrumbsIntent = new Intent(UserProfile.this, JSONRequest.class);
                    likeCrumbsIntent.putExtra(JSONRequest.IN_MSG, "getLikedCrumbs");
                    likeCrumbsIntent.putExtra("queryID", "getLikedCrumbs");
                    likeCrumbsIntent.putExtra("jsonObject", "{\"email\":\"" + email + "\"}");

                    startService(likeCrumbsIntent);

                } catch (JSONException j) {
                    j.printStackTrace();
                }

            }else if (responseType.trim().equalsIgnoreCase("getLikedCrumbs")) {

                    this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                    JSONArray tempJSONForLikes = new JSONArray();
                    try {
                        tempJSONForLikes = new JSONArray(response);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Get crumbs for this user failed.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    //array declarations that store all of the users' crumbs' attributes
                    usernameForLikes = new String[tempJSONForLikes.length()];
                    namesForLikes = new String[tempJSONForLikes.length()];
                    idsForLikes = new String[tempJSONForLikes.length()];
                    commentsForLikes = new String[tempJSONForLikes.length()];
                    tagsForLikes = new String[tempJSONForLikes.length()];
                    datesForLikes = new String[tempJSONForLikes.length()];
                    emailsForLikes = new String[tempJSONForLikes.length()];
                    rankForLikes = new Integer[tempJSONForLikes.length()];
                    upvotesForLikes = new Integer[tempJSONForLikes.length()];
                    longitudeForLikes = new Double[tempJSONForLikes.length()];
                    latitudeForLikes = new Double[tempJSONForLikes.length()];

                    try {
                        int likeCounter = 0;
                        //populate the arrays with each crumbs' attributes
                        for (int i = 0; i < tempJSONForLikes.length(); i++) {
                            usernameForLikes[i] = tempJSONForLikes.getJSONObject(i).getString("username");
                            namesForLikes[i] = tempJSONForLikes.getJSONObject(i).getString("crumbName");
                            idsForLikes[i] = tempJSONForLikes.getJSONObject(i).getString("crumbID");
                            commentsForLikes[i] = tempJSONForLikes.getJSONObject(i).getString("comment");
                            upvotesForLikes[i] = tempJSONForLikes.getJSONObject(i).getInt("upvotes");
                            longitudeForLikes[i] = tempJSONForLikes.getJSONObject(i).getDouble("longitude");
                            latitudeForLikes[i] = tempJSONForLikes.getJSONObject(i).getDouble("latitude");
                            datesForLikes[i] = tempJSONForLikes.getJSONObject(i).getString("crumbDate");
                            tagsForLikes[i] = tempJSONForLikes.getJSONObject(i).getString("crumbTags");
                            emailsForLikes[i] = tempJSONForLikes.getJSONObject(i).getString("email");
                            rankForLikes[i] = i+1;
                            likeCounter++;
                        }

                        TextView numDropped = (TextView) findViewById(R.id.tv_numOfLikes);
                        numDropped.setText("\n\n Number of crumbs upvoted: " + likeCounter);
                    } catch (JSONException j) {
                        j.printStackTrace();
                    }
            }
        }
    }
}
