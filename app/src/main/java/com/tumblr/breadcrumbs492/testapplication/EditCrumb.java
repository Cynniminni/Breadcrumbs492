package com.tumblr.breadcrumbs492.testapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

public class EditCrumb extends ActionBarActivity {
    private MyRequestReceiver6 receiver;
    private GoogleMap map;
    private String name;
    private String comment;
    private String tags;
    private String id;
    private EditText crumbName;
    private EditText crumbComment;
    private EditText crumbTags;
    private ToggleButton toggle;
    private TextView crumbDate;
    private TextView crumbUpvotes;
    private double longitude, latitude;
    private String visibility;
    private boolean isPrivate;
    public final static int REQUEST_SETTINGS = 0;

    public void editCrumb(View view) {
        name = crumbName.getText().toString();
        comment = crumbComment.getText().toString();
        tags = crumbTags.getText().toString();
        visibility = toggle.getText().toString();
        Intent intent = new Intent(this, MapsActivity.class);
        Intent intent2 = getIntent();
        id = intent2.getStringExtra(MyCrumbsActivity.CRUMB_ID);

        if(visibility.equals("Private"))
            isPrivate = true;
        else if(visibility.equals("Public"))
            isPrivate = false;

        if (name.equals("")) {
            //if user entered nothing then cancel adding a crumb
            setResult(RESULT_CANCELED, intent);
        } else {
            Intent msgIntent = new Intent(this, JSONRequest.class);
            msgIntent.putExtra(JSONRequest.IN_MSG, "editCrumb");
            msgIntent.putExtra("queryID", "editCrumb");

            msgIntent.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0] + "\",\"email\":\""
                    + GlobalContainer.user.getInfo()[1] + "\",\"crumbID\":\"" + id + "\",\"crumbName\":\"" + name
                    + "\",\"isPrivate\":\"" + isPrivate + "\",\"comment\":\"" + comment + "\",\"tags\":\"" + tags
                    + "\"}");
            msgIntent.putExtra("intent", intent.toUri(Intent.URI_INTENT_SCHEME));
            startService(msgIntent);

            //place into intent to pass back to MapsActivity
            intent.putExtra(MapsActivity.NAME, name);
            intent.putExtra(MapsActivity.COMMENT, comment);
            setResult(RESULT_OK, intent);//send result code
        }
    }

    public void deleteCrumb(View view){
        Intent intent3 = getIntent();
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Deleting Crumb")
                .setMessage("Are you sure you want to delete " + intent3.getStringExtra(MyCrumbsActivity.CRUMB_NAME) + "?")
                //setting ClickListener for selection of yes in alert dialog
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent = new Intent(EditCrumb.this.getApplicationContext(), MyCrumbsActivity.class);
                        Intent intent2 = getIntent();
                        id = intent2.getStringExtra(MyCrumbsActivity.CRUMB_ID);

                        Intent msgIntent = new Intent(EditCrumb.this.getApplicationContext(), JSONRequest.class);
                        msgIntent.putExtra(JSONRequest.IN_MSG, "deleteCrumb");
                        msgIntent.putExtra("queryID", "deleteCrumb");

                        msgIntent.putExtra("jsonObject", "{\"username\":\"" + GlobalContainer.user.getInfo()[0] + "\",\"crumbID\":\"" + id
                                + "\"}");
                        msgIntent.putExtra("intent", intent.toUri(Intent.URI_INTENT_SCHEME));
                        startService(msgIntent);
                        setResult(RESULT_OK, intent);//send result code
                        dialog.cancel();
                    }
                })
                //setting ClickListener for selection of no in alert dialog, stays on EditCrumb activity
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true)
                .show();
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

        //get references to the EditText, TextView and toggle fields
        crumbName = (EditText) findViewById(R.id.editcrumb_name);
        crumbComment = (EditText) findViewById(R.id.editcrumb_description);
        crumbTags = (EditText) findViewById(R.id.editcrumb_tags);
        toggle = (ToggleButton) findViewById(R.id.toggleButton2);

        isPrivate = getIntent().getBooleanExtra(MyCrumbsActivity.CRUMB_PRIVATE, true);
        if(isPrivate)
            toggle.setChecked(true);
        else
            toggle.setChecked(false);

        //populate EditTexts and TextViews  fields with selected crumb attributes
        Intent intent3 = getIntent();
        crumbName.setText(intent3.getStringExtra(MyCrumbsActivity.CRUMB_NAME));
        crumbComment.setText(intent3.getStringExtra(MyCrumbsActivity.CRUMB_COMMENT));
        crumbTags.setText(intent3.getStringExtra(MyCrumbsActivity.CRUMB_TAGS));

        //get longitude and latitude of crumb to edit to mark on map fragment
        longitude = intent3.getDoubleExtra(MyCrumbsActivity.CRUMB_LONGITUDE, 0.0);
        latitude = intent3.getDoubleExtra(MyCrumbsActivity.CRUMB_LATITUDE, 0.0);

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
                title(intent3.getStringExtra(MyCrumbsActivity.CRUMB_NAME)));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 12);
        map.animateCamera(yourLocation);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditCrumb.this, MyCrumbsActivity.class);
        startActivity(intent);
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
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // launch SettingsActivity with the request code. This will be referenced in
            // onActivityResult
            Intent intent = new Intent();
            intent.setClass(EditCrumb.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }

        if(id == R.id.action_logout_edit_crumb){
            GlobalContainer.user = new User();
            GlobalContainer.userIsInitialized = false;
            Intent logoutIntent = new Intent(EditCrumb.this, LoginActivity.class);
            startActivity(logoutIntent);
            EditCrumb.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyRequestReceiver6 extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.tumblr.breadcrumbs492.testapplication.EditCrumb.MyRequestReceiver";
        public String response = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseType = intent.getStringExtra(JSONRequest.IN_MSG);

            if (responseType.trim().equalsIgnoreCase("editCrumb")) {
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONObject tempJSON = new JSONObject();
                try {
                    tempJSON = new JSONObject(response);
                    if (tempJSON.getString("editCrumbResult").trim().equals("true")) {
                        Intent editCrumbIntent = new Intent(EditCrumb.this, MapsActivity.class);
                        Toast.makeText(getApplicationContext(), "Successfully edited crumb.", Toast.LENGTH_SHORT).show();
                        startActivity(editCrumbIntent);//close this activity and return to MapsActivity
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Editing crumb failed, please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else if (responseType.trim().equalsIgnoreCase("deleteCrumb")) {
                this.response = intent.getStringExtra(JSONRequest.OUT_MSG);
                JSONObject tempJSON = new JSONObject();
                try {
                    tempJSON = new JSONObject(response);
                    if (tempJSON.getString("deleteCrumbResult").trim().equals("true")) {
                        Intent editCrumbIntent = new Intent(EditCrumb.this, MapsActivity.class);
                        Toast.makeText(getApplicationContext(), "Successfully deleted crumb.", Toast.LENGTH_SHORT).show();
                        startActivity(editCrumbIntent);//close this activity and return to MapsActivity
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Deleting crumb failed, please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }

        }
    }
}
