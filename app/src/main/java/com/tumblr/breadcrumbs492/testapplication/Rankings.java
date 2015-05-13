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

public class Rankings extends ActionBarActivity {

    private MyRequestReceiver10 receiver;
    public final static int REQUEST_SETTINGS = 0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver10.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver10();
        registerReceiver(receiver, filter);

        Intent msgIntent = new Intent(this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getCrumbsRanked");
        msgIntent.putExtra("queryID", "getCrumbsRanked");
        msgIntent.putExtra("jsonObject", "{\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");

        startService(msgIntent);
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
    public void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rankings, menu);
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
            intent.setClass(Rankings.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }

        if(id == R.id.action_logout_rankings){
            GlobalContainer.user = new User();
            GlobalContainer.userIsInitialized = false;
            Intent logoutIntent = new Intent(Rankings.this, LoginActivity.class);
            startActivity(logoutIntent);
            Rankings.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //broadcast receiver to receive messages sent from the JSON IntentService
    public class MyRequestReceiver10 extends BroadcastReceiver {
        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.MapsActivity.MyRequestReceiver";
        public String response = null;
        private ListView listView;

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);
            if (responseType.trim().equalsIgnoreCase("getCrumbsRanked")) {
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
                        rank[i] = (i + 1);
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }

                //define adapter to populate each row in ListView
                CustomListAdapter2 adapter = new CustomListAdapter2(Rankings.this, names, dates, rank, upvotes, username);
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
                        //attributes of crumb selected and passed to CrumbDetails activity
                        Intent intent = new Intent(Rankings.this, CrumbDetails.class);
                        intent.putExtra(CRUMB_NAME, names[itemPosition]);
                        intent.putExtra(CRUMB_ID, ids[itemPosition]);
                        intent.putExtra(CRUMB_COMMENT, comments[itemPosition]);
                        intent.putExtra(CRUMBS_TAGS, tags[itemPosition]);
                        intent.putExtra(CRUMB_UPVOTES, upvotes[itemPosition]);
                        intent.putExtra(CRUMB_DATE, dates[itemPosition]);
                        intent.putExtra(SEARCH, getIntent().getStringExtra(MapsActivity.SEARCH));
                        intent.putExtra(USERNAME, username[itemPosition]);
                        intent.putExtra(CRUMB_LONGITUDE, longitude[itemPosition]);
                        intent.putExtra(CRUMB_LATITUDE, latitude[itemPosition]);
                        intent.putExtra(CRUMB_EMAIL, emails[itemPosition]);
                        intent.putExtra("activity", "Rankings");

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
