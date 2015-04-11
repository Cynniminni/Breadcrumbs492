package com.tumblr.breadcrumbs492.testapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Matt on 4/10/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] crumbName;
    private final Date[] crumbDate;
    private final Integer[] crumbIMGID;

    public CustomListAdapter(Activity context, String[] crumbName, Date[] crumbDate, Integer[] crumbIMGID) {
        super(context, R.layout.crumblist, crumbName);
        this.context = context;
        this.crumbName = crumbName;
        this.crumbDate = crumbDate;
        this.crumbIMGID = crumbIMGID;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.crumblist, null, true);
        TextView crumbName2 = (TextView) rowView.findViewById(R.id.crumbName);
        TextView crumbDate2 = (TextView) rowView.findViewById(R.id.crumbDate);
        ImageView crumbIMGID2 = (ImageView) rowView.findViewById(R.id.crumbImage);

        crumbName2.setText(crumbName[position]);
        crumbDate2.setText(crumbDate[position].toString());
        crumbIMGID2.setImageResource(R.drawable.com_facebook_logo);
        return rowView;
    };

}
