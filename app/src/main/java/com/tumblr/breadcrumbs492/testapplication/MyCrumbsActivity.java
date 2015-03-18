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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class MyCrumbsActivity extends ActionBarActivity {


    private String username;
    private MyRequestReceiver3 receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crumbs);

        username = getIntent().getStringExtra("username");

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver3.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver3();
        registerReceiver(receiver, filter);

        //populate user information fields through database
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        Intent msgIntent = new Intent(this, JSONRequest.class);
        msgIntent.putExtra(JSONRequest.IN_MSG, "getCrumbs");
        msgIntent.putExtra("queryID", "getCrumbs");
        msgIntent.putExtra("jsonObject", "{\"username\":\"" + username + "\"}");

        startService(msgIntent);


    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
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
                System.out.println(response);
                JSONArray tempJSON = new JSONArray();
                try {
                    tempJSON = new JSONArray(response);
                    System.out.println(tempJSON.toString());

                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "get crumbs failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
//get ListView reference from xml
                listView = (ListView) findViewById(R.id.list);
                String[] values = new String[tempJSON.length()];
                try {
                    //define a sample array of values
                    //this will be replaced later with a list of crumb names from db
                    for (int i = 0; i < tempJSON.length(); i++) {
                        values[i] = tempJSON.getJSONObject(i).getString("crumbName");

                    }
                }
                catch(JSONException j)
                {
                    j.printStackTrace();
                }

                //define adapter to populate each row in ListView
        /*
            simple_list_item_1 is a built-in Android template that shows only one line of text
            for each row. I want to show both crumb name and date created
         */
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (MyCrumbsActivity.this, android.R.layout.simple_list_item_1, values);
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
                        Toast.makeText(getApplicationContext(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue,
                                Toast.LENGTH_SHORT).show();
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
