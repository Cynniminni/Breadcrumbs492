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
        View rowView = inflater.inflate(R.layout.crumblist2, null, true);
        TextView crumbName2 = (TextView) rowView.findViewById(R.id.crumbName);
        TextView crumbDate2 = (TextView) rowView.findViewById(R.id.crumbDate);
        TextView crumbRank2 = (TextView) rowView.findViewById(R.id.crumbRank);
        TextView crumbUser2 = (TextView) rowView.findViewById(R.id.crumbUser);
        TextView crumbUpvotes2 = (TextView) rowView.findViewById(R.id.crumbUpvotes);

        //ImageView crumbIMGID2 = (ImageView) rowView.findViewById(R.id.crumbImage);
        //Button deleteButton = (Button) rowView.findViewById(R.id.deleteCrumb);
        //((Button) view.findViewById(R.id.deleteCrumb)).setOnClickListener(deleteButtonClickListener);

        crumbName2.setText(crumbName[position]);
        crumbDate2.setText(crumbDate[position]);
        crumbRank2.setText(String.valueOf(crumbRank[position]));
        crumbUser2.setText("Dropped by: " + crumbUser[position]);
        crumbUpvotes2.setText(String.valueOf(crumbUpvotes[position]));
        return rowView;
    }


    /*private OnClickListener deleteButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //final int position  = getListView().getPositio
        }
    };*/
}
