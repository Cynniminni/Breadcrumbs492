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
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar;


public class MyCrumbsActivity extends ActionBarActivity {


    private MyRequestReceiver3 receiver;
    public final static String CRUMB_NAME = "crumbName";
    public final static String CRUMB_COMMENT = "crumbComment";
    public final static String CRUMB_TAGS = "crumbsTags";
    public final static String CRUMB_ID = "crumbID";
    public final static String CRUMB_UPVOTES = "crumbUpvotes";
    public final static String CRUMB_DATE = "crumbDate";
    public final static String CRUMB_LONGITUDE = "longitude";
    public final static String CRUMB_LATITUDE = "latitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crumbs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver3.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver3();
        registerReceiver(receiver, filter);

        Intent msgIntent = new Intent(this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getCrumbs");
        msgIntent.putExtra("queryID", "getCrumbs");
        msgIntent.putExtra("jsonObject", "{\"email\":\"" + GlobalContainer.user.getInfo()[1] + "\"}");

        startService(msgIntent);


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
        getMenuInflater().inflate(R.menu.menu_my_crumbs, menu);
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
    public class MyRequestReceiver3 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.MyCrumbsActivity.MyRequestReceiver";
        public String response = null;
        private ListView listView;

        @Override
        public void onReceive(Context context, Intent intent) {

            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            if(responseType.trim().equalsIgnoreCase("getCrumbs")){

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                System.out.println(this.response);

                JSONArray tempJSON = new JSONArray();
                try {
                    tempJSON = new JSONArray(response);
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "get crumbs failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                //get ListView reference from xml
                listView = (ListView) findViewById(R.id.list);
                //array declarations that store all of the users' crumbs' attributes
                final String[] names = new String[tempJSON.length()];
                final String[] ids = new String[tempJSON.length()];
                final String[] comments = new String[tempJSON.length()];
                final String[] tags = new String[tempJSON.length()];
                final String[] dates = new String[tempJSON.length()];
                final Integer[] imgID = new Integer[1];
                final Integer[] upvotes = new Integer[tempJSON.length()];
                final Double[] longitude = new Double[tempJSON.length()];
                final Double[] latitude = new Double[tempJSON.length()];

                try {
                    //populate the arrays with each crumbs' attributes
                    for (int i = 0; i < tempJSON.length(); i++) {
                        names[i] = tempJSON.getJSONObject(i).getString("crumbName");
                        ids[i] = tempJSON.getJSONObject(i).getString("crumbID");
                        comments[i] = tempJSON.getJSONObject(i).getString("comment");
                        tags[i] = tempJSON.getJSONObject(i).getString("tags");
                        upvotes[i] = tempJSON.getJSONObject(i).getInt("upvotes");
                        longitude[i] = tempJSON.getJSONObject(i).getDouble("longitude");
                        latitude[i] = tempJSON.getJSONObject(i).getDouble("latitude");
                        dates[i] = tempJSON.getJSONObject(i).getString("crumbDate");

                        /*String dateString = tempJSON.getJSONObject(i).getString("crumbDate");
                        System.out.println("Date string for mycrumbs: " + dateString);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        try {
                            Date crumbDate = sdf.parse(dateString);
                            System.out.println("After string parse to date in mycrumbs: " + crumbDate.toString());
                            String newDate = sdf.format(crumbDate);
                            System.out.println("Date to format in mycrumbs: " + newDate);
                            dates[i] = crumbDate;
                        }catch(ParseException e){
                            e.printStackTrace();
                        }*/
                    }
                }
                catch(JSONException j)
                {
                    j.printStackTrace();
                }

                //define adapter to populate each row in ListView

                CustomListAdapter adapter = new CustomListAdapter(MyCrumbsActivity.this, names, dates, imgID);
                listView.setAdapter(adapter);


        /*
            simple_list_item_1 is a built-in Android template that shows only one line of text
            for each row. I want to show both crumb name and date created
         */
               /* ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (MyCrumbsActivity.this, android.R.layout.simple_list_item_1, names);
                listView.setAdapter(adapter);*/

                //add ListView item click listener to interact with each item
                listView.setOnItemClickListener(new OnItemClickListener(){
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
                        Intent intent = new Intent(MyCrumbsActivity.this, EditCrumb.class);
                        intent.putExtra(CRUMB_NAME, names[itemPosition]);
                        intent.putExtra(CRUMB_ID, ids[itemPosition]);
                        intent.putExtra(CRUMB_COMMENT, comments[itemPosition]);
                        intent.putExtra(CRUMB_TAGS, tags[itemPosition]);
                        intent.putExtra(CRUMB_UPVOTES, upvotes[itemPosition]);
                        intent.putExtra(CRUMB_DATE, dates[itemPosition]);
                        intent.putExtra(CRUMB_LONGITUDE, longitude[itemPosition]);
                        intent.putExtra(CRUMB_LATITUDE, latitude[itemPosition]);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            else{
                //you can choose to implement another transaction here
            }

        }
        public String getResponse()
        {
            return response;
        }
    }
}
