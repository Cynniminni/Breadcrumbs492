package com.tumblr.breadcrumbs492.testapplication;

/**
 * Created by scott on 3/20/2015.
 */
public class User {
    private static String username;
    private static String firstName;
    private static String lastName;
    private static String email;
    private static String gender;
    private static String city;
    private static String state;

    public User(){
        username = "";
        firstName = "";
        lastName = "";
        email = "";
        gender = "";
        city = "";
        state = "";
    };

    public User(String _username, String _email, String _firstName, String _lastName, String _gender, String _city, String _state){
        username = _username;
        firstName = _firstName;
        lastName = _lastName;
        email = _email;
        gender = _gender;
        city = _city;
        state = _state;
    }

    public static String[] getInfo(){
        String [] arr = {username,email,firstName,lastName,gender,city,state};

        return arr;
    }
}
