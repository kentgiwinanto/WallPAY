package com.example.javerill.wailpay;

/**
 * Created by javerill on 9/22/2016.
 */

public class User {

    //Private Variables
    int id;
    String fullName;
    String email;
    String userID;
    String password;

    // Empty Constructor
    public User(){

    }

    // Constructor
    public User(int id, String fullName, String email, String userID, String password){
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.userID = userID;
        this.password = password;
    }

    // Constructor
    public User(String fullName, String email, String userID, String password){
        this.fullName = fullName;
        this.email = email;
        this.userID = userID;
        this.password = password;
    }

    // Getting ID
    public int getID(){
        return this.id;
    }

    // Setting id
    public void setID(int id){
        this.id = id;
    }

    // Getting Name
    public String getFullName(){
        return this.fullName;
    }

    // Setting Name
    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    // Getting Email
    public String getEmail(){
        return this.email;
    }

    // Setting Email
    public void setEmail(String email){
        this.email = email;
    }

    // Getting User ID
    public String getUserID(){
        return this.userID;
    }

    // Setting User ID
    public void setUserID(String userID){
        this.userID = userID;
    }

    // Getting Password
    public String getPassword(){
        return this.password;
    }

    // Setting Password
    public void setPassword(String password){
        this.password = password;
    }
}
