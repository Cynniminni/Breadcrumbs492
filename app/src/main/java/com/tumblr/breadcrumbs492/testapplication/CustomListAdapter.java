package com.tumblr.breadcrumbs492.testapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

/**
 * Created by Matt on 4/10/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] crumbName;
    private final String[] crumbDate;
    private final Integer[] crumbIMGID;

    public CustomListAdapter(Activity context, String[] crumbName, String[] crumbDate, Integer[] crumbIMGID) {
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

        String tempDate = crumbDate[position].substring(0,10);
        crumbName2.setText(crumbName[position]);
        crumbDate2.setText("\t\t" + tempDate);
        crumbIMGID2.setImageResource(R.mipmap.breadcrumb_launcher);

        return rowView;
    }
}
