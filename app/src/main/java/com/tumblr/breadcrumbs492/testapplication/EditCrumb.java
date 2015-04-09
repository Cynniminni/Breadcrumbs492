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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;


public class EditCrumb extends ActionBarActivity {

    private MyRequestReceiver6 receiver;
    private String name;
    private String comment;
    private String tags;
    private String id;
    private EditText crumbName;
    private EditText crumbComment;
    private EditText crumbTags;

    public void editCrumb(View view) {
        name = crumbName.getText().toString();
        comment = crumbComment.getText().toString();
        tags = crumbTags.getText().toString();
        Intent intent = new Intent(this, MapsActivity.class);
        Intent intent2 = getIntent();
        id = intent2.getStringExtra(MyCrumbsActivity.CRUMB_ID);

        if (name.equals("")) {
            //if user entered nothing then cancel adding a crumb
            setResult(RESULT_CANCELED, intent);
        } else {
            Intent msgIntent = new Intent(this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "editCrumb");
            msgIntent.putExtra("queryID", "editCrumb");

            msgIntent.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0] + "\",\"email\":\""
                    + GlobalContainer.user.getInfo()[1] + "\",\"crumbID\":\"" + id + "\",\"crumbName\":\"" + name
                    + "\",\"comment\":\"" + comment + "\",\"tags\":\"" + tags
                    + "\"}");
            msgIntent.putExtra("intent", intent.toUri(Intent.URI_INTENT_SCHEME));
            startService(msgIntent);

            //place into intent to pass back to MapsActivity
            intent.putExtra(MapsActivity.NAME, name);
            intent.putExtra(MapsActivity.COMMENT, comment);
            setResult(RESULT_OK, intent);//send result code
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_crumb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Register your receiver so that the Activity can be notified
        //when the JSON response came back
        IntentFilter filter = new IntentFilter(MyRequestReceiver6.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyRequestReceiver6();
        registerReceiver(receiver, filter);

        //get references to the EditText fields
        crumbName = (EditText) findViewById(R.id.editcrumb_name);
        crumbComment = (EditText) findViewById(R.id.editcrumb_comment);
        crumbTags = (EditText) findViewById(R.id.editcrumb_tags);

        //populate edittext fields with selected crumb attributes
        Intent intent3 = getIntent();
        crumbName.setText(intent3.getStringExtra(MyCrumbsActivity.CRUMB_NAME));
        crumbComment.setText(intent3.getStringExtra(MyCrumbsActivity.CRUMB_COMMENT));
        crumbTags.setText(intent3.getStringExtra(MyCrumbsActivity.CRUMB_TAGS));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_crumb, menu);
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

    public class MyRequestReceiver6 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.AddCrumbActivity.MyRequestReceiver";
        public String response = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);
            Intent editCrumbIntent = new Intent(EditCrumb.this, MapsActivity.class);
            if (responseType.trim().equalsIgnoreCase("editCrumb")) {

                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);

                JSONObject tempJSON = new JSONObject();

                try {
                    tempJSON = new JSONObject(response);
                    if (tempJSON.getString("editCrumbResult").trim().equals("true")) {
                        Toast.makeText(getApplicationContext(), "successfully edited crumb", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "edit crumb failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                startActivity(editCrumbIntent);//close this activity and return to MapsActivity
                finish();

            } else {
                //you can choose to implement another transaction here
            }

        }
    }
}
