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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONArray;


public class UserProfile extends ActionBarActivity {
    private MyRequestReceiver8 receiver;
    private String search, email;
    public final static String CRUMB_NAME = "crumbName";
    public final static String CRUMB_COMMENT = "crumbComment";
    public final static String CRUMB_TAGS = "crumbTags";
    public final static String CRUMB_ID = "crumbID";
    public final static String CRUMB_UPVOTES = "upvotes";
    public final static String CRUMB_DATE = "crumbDate";
    public final static String SEARCH = "search";
    public final static String USERNAME = "username";
    public final static String CRUMB_LONGITUDE = "longitude";
    public final static String CRUMB_LATITUDE = "latitude";
    public final static String EMAIL = "email";

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

        search = getIntent().getStringExtra(CrumbDetails.SEARCH);
        email = getIntent().getStringExtra(CrumbDetails.EMAIL);
        System.out.println("Email: " + email);


        Intent msgIntent = new Intent(this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getUserCrumbs");
        msgIntent.putExtra("queryID", "getUserCrumbs");
        msgIntent.putExtra("jsonObject", "{\"email\":\"" + email + "\"}");

        startService(msgIntent);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CrumbDetails.class);
        intent.putExtra(EMAIL, getIntent().getStringExtra(CrumbDetails.EMAIL));
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
        intent.putExtra("activity", "UserProfile");
        setResult(RESULT_OK, intent);
        startActivity(intent);
        System.out.println("tagsuserprofile: " + getIntent().getStringExtra(CrumbDetails.CRUMB_TAGS));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver8 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.UserProfile.MyRequestReceiver";
        public String response = null;
        private ListView listView;

        @Override
        public void onReceive(Context context, Intent intent) {

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            if (responseType.trim().equalsIgnoreCase("getUserCrumbs")) {

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                System.out.println("This is the response from userprofile: " + this.response);

                JSONArray tempJSON = new JSONArray();
                try {
                    tempJSON = new JSONArray(response);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Get crumbs for this user failed.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                //get ListView reference from xml
                listView = (ListView) findViewById(R.id.list);
                //array declarations that store all of the users' crumbs' attributes
                final String[] username = new String[tempJSON.length()];
                final String[] names = new String[tempJSON.length()];
                final String[] ids = new String[tempJSON.length()];
                final String[] comments = new String[tempJSON.length()];
                final String[] tags = new String[tempJSON.length()];
                final String[] dates = new String[tempJSON.length()];
                final String[] emails = new String[tempJSON.length()];
                final Integer[] imgID = new Integer[1];
                final Integer[] upvotes = new Integer[tempJSON.length()];
                final Double[] longitude = new Double[tempJSON.length()];
                final Double[] latitude = new Double[tempJSON.length()];

                try {
                    //populate the arrays with each crumbs' attributes
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

                        /*String dateString = tempJSON.getJSONObject(i).getString("crumbDate");
                        System.out.println("Date string in search results: " + dateString);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        try {
                            Date crumbDate = sdf.parse(dateString);
                            System.out.println("After string parse to date in searchresults: " + crumbDate.toString());
                            String newDate = sdf.format(crumbDate);
                            System.out.println("Date to format in searchresults: " + newDate);
                            dates[i] = crumbDate;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }

                //define adapter to populate each row in ListView

                CustomListAdapter adapter = new CustomListAdapter(UserProfile.this, names, dates, imgID);
                listView.setAdapter(adapter);


        /*
            simple_list_item_1 is a built-in Android template that shows only one line of text
            for each row. I want to show both crumb name and date created
         */
               /* ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (MyCrumbsActivity.this, android.R.layout.simple_list_item_1, names);
                listView.setAdapter(adapter);*/

                //add ListView item click listener to interact with each item
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //get position of item clicked
                        int itemPosition = position;

                        //get value of item clicked
                        String itemValue = (String) listView.getItemAtPosition(position);

                        //output item
                        Toast.makeText(getApplicationContext(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue,
                                Toast.LENGTH_SHORT).show();
                        //position that was clicked determines array element to retrieve
                        //attributes of crumb selected and passed to EditCrumb activity
                        Intent intent = new Intent(UserProfile.this, CrumbDetails.class);
                        intent.putExtra(CRUMB_NAME, names[itemPosition]);
                        intent.putExtra(CRUMB_ID, ids[itemPosition]);
                        intent.putExtra(CRUMB_COMMENT, comments[itemPosition]);
                        intent.putExtra(CRUMB_TAGS, tags[itemPosition]);
                        intent.putExtra(CRUMB_UPVOTES, upvotes[itemPosition]);
                        intent.putExtra(CRUMB_DATE, dates[itemPosition]);
                        intent.putExtra(SEARCH, getIntent().getStringExtra(MapsActivity.SEARCH));
                        intent.putExtra(USERNAME, username[itemPosition]);
                        intent.putExtra(CRUMB_LONGITUDE, longitude[itemPosition]);
                        intent.putExtra(CRUMB_LATITUDE, latitude[itemPosition]);
                        intent.putExtra(EMAIL, emails[itemPosition]);
                        intent.putExtra("activity", "UserProfile");

                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                //you can choose to implement another transaction here
            }
        }

    }
}
