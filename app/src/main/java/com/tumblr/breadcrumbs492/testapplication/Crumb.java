package com.tumblr.breadcrumbs492.testapplication;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aphrodite on 3/15/2015.
 */
public class Crumb {
    private String name, comment;
    private LatLng location;
    private String date;
    // rating is how many upvotes a crumb has received
    private int rating;
    // score will be some function of upvotes and downvotes but will not be shown
    // it is used to determine placement when sorting
    private int score;

    //default upvote/downvote values for all crumbs are 0

    public Crumb() {
        name = "Default Crumb";
        comment = "Default comment";
        location = new LatLng(0, 0);
        date = " ";
        rating = 0;
        score = 0;
    }

    public Crumb(String name, String comment, LatLng location, String date, int rating) {
        this.name = name;
        this.comment = comment;
        this.location = location;
        this.date = date;
        this.rating = rating;
        score = 0;
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

    public String getDate() {
        return date;
    }

    public int getRating() {
        return rating;
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

    public void setDate(String date) {
        this.date = date;
    }

    // Will probably only use these for testing
    public void setRating(int rating) {
        this.rating = rating;
    }
}
