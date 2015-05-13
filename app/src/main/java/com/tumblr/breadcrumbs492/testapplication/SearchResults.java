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
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;


public class SearchResults extends ActionBarActivity {
    private MyRequestReceiver7 receiver;
    private String search;
    public final static String CRUMB_NAME = "crumbName";
    public final static String CRUMB_COMMENT = "crumbComment";
    public final static String CRUMB_TAGS = "crumbTags";
    public final static String CRUMB_ID = "crumbID";
    public final static String CRUMB_UPVOTES = "crumbUpvotes";
    public final static String CRUMB_DATE = "crumbDate";
    public final static String SEARCH = "search";
    public final static String USERNAME = "username";
    public final static String CRUMB_LONGITUDE = "longitude";
    public final static String CRUMB_LATITUDE = "latitude";
    public final static String EMAIL = "email";
    public final static int REQUEST_SETTINGS = 0;

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

        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(id == R.id.action_logout_search_results){
            GlobalContainer.user = new User();
            GlobalContainer.userIsInitialized = false;
            Intent logoutIntent = new Intent(SearchResults.this, LoginActivity.class);
            startActivity(logoutIntent);
            SearchResults.this.finish();
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
                final String[] username = new String[tempJSON.length()];
                final String[] names = new String[tempJSON.length()];
                final String[] ids = new String[tempJSON.length()];
                final String[] comments = new String[tempJSON.length()];
                final String[] tags = new String[tempJSON.length()];
                final String[] dates = new String[tempJSON.length()];
                final String[] emails = new String[tempJSON.length()];
                final Integer[] rank = new Integer[tempJSON.length()];
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
                        rank[i] = i + 1;
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }

                //define adapter to populate each row in ListView
                CustomListAdapter2 adapter = new CustomListAdapter2(SearchResults.this, names, dates, rank, upvotes, username);
                listView.setAdapter(adapter);

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
                        Intent intent = new Intent(SearchResults.this, CrumbDetails.class);
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
                        intent.putExtra("activity", "SearchResults");

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
