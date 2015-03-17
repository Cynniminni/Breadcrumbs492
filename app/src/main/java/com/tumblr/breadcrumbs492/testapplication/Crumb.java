package com.tumblr.breadcrumbs492.testapplication;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;

import java.util.Date;

/**
 * Created by Aphrodite on 3/15/2015.
 */
public class Crumb {
    private String name, comment;
    private LatLng location;
    private Date date;
    private int upvote, downvote;

<<<<<<< HEAD
    // TODO: Needs support for custom images, and news feed (Google Maps API Info Windows?)
=======
    //default upvote/downvote values for all crumbs are 0
>>>>>>> d922682707462cd5ddc6198c4b9466eff35aae92

    public Crumb() {
        name = "Default Crumb";
        comment = "Default comment";
        location = new LatLng(0, 0);
        date = new Date();
        upvote = 0;
        downvote = 0;
    }

<<<<<<< HEAD
    private LatLng location;
    private String title;
    private String infoSnippet;
    private Date date;
    private int upvote, downvote;
    // The map this crumb exists on
    private GoogleMap map;

    public Crumb()
    {
        location = new LatLng(0, 0);
        title = "";
        infoSnippet = "";
        date = new Date();
        upvote = 0;
        downvote = 0;
        map = null;
    }

    public Crumb(LatLng loc, String crumbTitle, String snippet, GoogleMap ownedMap, Date date)
    {
        location = loc;
        title = crumbTitle;
        infoSnippet = snippet;
        map = ownedMap;
        this.date = date;
=======
    public Crumb(String name, String comment, LatLng location, Date date) {
        this.name = name;
        this.comment = comment;
        this.location = location;
        this.date = date;
        upvote = 0;
        downvote = 0;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public LatLng getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public int getUpvote() {
        return upvote;
    }

    public int getDownvote() {
        return downvote;
    }
>>>>>>> d922682707462cd5ddc6198c4b9466eff35aae92

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

<<<<<<< HEAD
        // TODO: adds crumb info to the database
    }

    public LatLng getCoords()
    {
        return location;
=======
    public void setLocation(LatLng location) {
        this.location = location;
>>>>>>> d922682707462cd5ddc6198c4b9466eff35aae92
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }
}
