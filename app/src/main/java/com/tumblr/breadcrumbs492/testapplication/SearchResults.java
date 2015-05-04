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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SearchResults extends ActionBarActivity {
    private MyRequestReceiver7 receiver;
    private String search;
    public final static String CRUMB_NAME = "crumbName";
    public final static String CRUMB_COMMENT = "crumbComment";
    public final static String CRUMB_TAGS = "crumbsTags";
    public final static String CRUMB_ID = "crumbID";
    public final static String CRUMB_RATING = "crumbRating";
    public final static String CRUMB_DATE = "crumbDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver7.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver7();
        registerReceiver(receiver, filter);

        //get users search input that was passed from MapActivity
        search = getIntent().getStringExtra(MapsActivity.SEARCH);
        System.out.println(search);

        if (search != null && !search.equals("")) {
            //if location exists, mark it on map
            //new GeocoderTask().execute(location);
            //not using Geocoder class will remove the Google location searches

            //insert code to find tags
            Intent msgIntent = new Intent(SearchResults.this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "findTagsResults");
            msgIntent.putExtra("queryID", "findTagsResults");
            msgIntent.putExtra("jsonObject", "{\"tag\":\"" + search.trim() + "\"}");
            startService(msgIntent);
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
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
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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
    public class MyRequestReceiver7 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.SearchResults.MyRequestReceiver";
        public String response = null;
        private ListView listView;

        @Override
        public void onReceive(Context context, Intent intent) {

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            if (responseType.trim().equalsIgnoreCase("findTagsResults")) {

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                System.out.println("This is the response: " + this.response);

                JSONArray tempJSON = new JSONArray();
                try {
                    tempJSON = new JSONArray(response);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Get crumbs for this tag failed.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                //get ListView reference from xml
                listView = (ListView) findViewById(R.id.list);
                //array declarations that store all of the users' crumbs' attributes
                final String[] names = new String[tempJSON.length()];
                final String[] ids = new String[tempJSON.length()];
                final String[] comments = new String[tempJSON.length()];
                final String[] tags = new String[tempJSON.length()];
                final Date[] dates = new Date[tempJSON.length()];
                final Integer[] imgID = new Integer[1];
                final Integer[] ratings = new Integer[tempJSON.length()];

                try {
                    //populate the arrays with each crumbs' attributes
                    for (int i = 0; i < tempJSON.length(); i++) {
                        names[i] = tempJSON.getJSONObject(i).getString("crumbName");
                        ids[i] = tempJSON.getJSONObject(i).getString("crumbID");
                        comments[i] = tempJSON.getJSONObject(i).getString("comment");
                        tags[i] = tempJSON.getJSONObject(i).getString("tags");
                        ratings[i] = tempJSON.getJSONObject(i).getInt("rating");

                        String dateString = tempJSON.getJSONObject(i).getString("date");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date crumbDate = sdf.parse(dateString);
                            dates[i] = crumbDate;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }

                //define adapter to populate each row in ListView

                CustomListAdapter adapter = new CustomListAdapter(SearchResults.this, names, dates, imgID);
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
                        Intent intent = new Intent(SearchResults.this, MapsActivity.class);
                        intent.putExtra(CRUMB_NAME, names[itemPosition]);
                        intent.putExtra(CRUMB_ID, ids[itemPosition]);
                        intent.putExtra(CRUMB_COMMENT, comments[itemPosition]);
                        intent.putExtra(CRUMB_TAGS, tags[itemPosition]);
                        intent.putExtra(CRUMB_RATING, ratings[itemPosition]);
                        intent.putExtra(CRUMB_DATE, dates[itemPosition]);
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