package eu.kudan.rahasianusantara.model;

import java.util.ArrayList;

/**
 * Created by fauza on 6/5/2017.
 */

public class Mission {
    public static final int MISSION_FAILED = -1;
    public static final int MISSION_PENDING = 0;
    public static final int MISSION_ACCESSIBLE = 1;
    public static final int MISSION_SUCCESS = 2;
    private int id;
    private String title;
    private int status;
    private ArrayList<Integer> next;
    private ArrayList<Integer> prev;
    private double longitude;
    private double latitude;
    private ArrayList<String> dialogue;

    public Mission(){
        next = new ArrayList<Integer>();
        prev = new ArrayList<Integer>();
        dialogue = new ArrayList<String>();
    }

    public Mission(int id, String title, int status, ArrayList<Integer> next, ArrayList<Integer> prev, double longitude, double latitude, ArrayList<String> dialogue) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.next = next;
        this.prev = prev;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dialogue = dialogue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Integer> getNext() {
        return next;
    }

    public void setNext(ArrayList<Integer> next) {
        this.next = next;
    }

    public ArrayList<Integer> getPrev() {
        return prev;
    }

    public void setPrev(ArrayList<Integer> prev) {
        this.prev = prev;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ArrayList<String> getDialogue() {
        return dialogue;
    }

    public void setDialogue(ArrayList<String> dialogue) {
        this.dialogue = dialogue;
    }
}
