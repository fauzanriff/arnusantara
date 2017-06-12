package eu.kudan.rahasianusantara.model;

import java.util.ArrayList;

/**
 * Created by fauza on 6/5/2017.
 */

public class User {
    private final static int EXPERIENCE_LEVEL = 100;
    private String id;
    private String username;
    private String email;
    private String picture;
    private int level;
    private int exp;
    private int achievement;
    private ArrayList<Integer> myquest;

    public User(){}

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.picture = "";
        this.level = 0;
        this.exp = 0;
    }

    public User(String id, String username, String email, String picture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.level = 0;
        this.exp = 0;
        this.achievement = 0;
    }

    public User(String id, String username, String email, String picture, int level, int exp, int achievement) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.level = level;
        this.exp = exp;
        this.achievement = achievement;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void addExp(int amount){
        exp += amount;
        if (exp > EXPERIENCE_LEVEL){
            level += exp/EXPERIENCE_LEVEL;
            exp = exp%EXPERIENCE_LEVEL;
        }
    }

    public int getAchievement() {
        return achievement;
    }

    public void setAchievement(int achievement) {
        this.achievement = achievement;
    }

    public void addAchievement(){
        this.achievement++;
    }

    public ArrayList<Integer> getMyquest() {
        return myquest;
    }

    public void setMyquest(ArrayList<Integer> myquest) {
        this.myquest = myquest;
    }
}
