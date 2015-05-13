package com.tumblr.breadcrumbs492.testapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;


/**
 * Created by Matt on 5/9/2015.
 */
public class CustomListAdapter2 extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] crumbName;
    private final String[] crumbDate;
    private final Integer[] crumbRank;
    private final Integer[] crumbUpvotes;
    private final String[] crumbUser;

    public CustomListAdapter2(Activity context, String[] crumbName, String[] crumbDate, Integer[] crumbRank, Integer[] crumbUpvotes, String[] crumbUser ) {
        super(context, R.layout.crumblist2, crumbName);
        this.context = context;
        this.crumbName = crumbName;
        this.crumbDate = crumbDate;
        this.crumbRank = crumbRank;
        this.crumbUpvotes = crumbUpvotes;
        this.crumbUser = crumbUser;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.crumblist2, parent, false);
        TextView crumbName2 = (TextView) rowView.findViewById(R.id.crumbName);
        TextView crumbDate2 = (TextView) rowView.findViewById(R.id.crumbDate);
        TextView crumbRank2 = (TextView) rowView.findViewById(R.id.crumbRank);
        TextView crumbUser2 = (TextView) rowView.findViewById(R.id.crumbUser);
        TextView crumbUpvotes2 = (TextView) rowView.findViewById(R.id.crumbUpvotes);
        String tempCrumbDate = crumbDate[position].substring(0,10);
        crumbName2.setText(crumbName[position]);
        crumbDate2.setText("\t" + "Dropped on: " + tempCrumbDate);
        crumbRank2.setText(String.valueOf(crumbRank[position]));
        crumbUser2.setText("\t" + "Dropped by: " + crumbUser[position]);
        crumbUpvotes2.setText(String.valueOf(crumbUpvotes[position]));
        return rowView;
    }
}
