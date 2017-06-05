package eu.kudan.rahasianusantara.model;

import java.util.ArrayList;

/**
 * Created by fauza on 6/5/2017.
 */

public class User {
    private String id;
    private String username;
    private String email;
    private ArrayList<Integer> myquest;

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Integer> getMyquest() {
        return myquest;
    }

    public void setMyquest(ArrayList<Integer> myquest) {
        this.myquest = myquest;
    }
}
