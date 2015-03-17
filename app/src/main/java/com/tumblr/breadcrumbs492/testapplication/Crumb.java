package com.tumblr.breadcrumbs492.testapplication;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Aphrodite on 3/15/2015.
 */
public class Crumb {
    public String name, comment;
    public LatLng location;
    public Date date;
    public int upvote, downvote;

    //default upvote/downvote values for all crumbs are 0

    public Crumb() {
        name = "Default Crumb";
        comment = "Default comment";
        location = new LatLng(0, 0);
        date = new Date();
        upvote = 0;
        downvote = 0;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLocation(LatLng location) {
        this.location = location;
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
