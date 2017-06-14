package eu.kudan.rahasianusantara.model;

import android.location.Location;
import android.location.LocationManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fauza on 6/5/2017.
 */

public class Quest implements Serializable{
    public static final int QUEST_FAILED = -1;
    public static final int QUEST_PENDING = 0;
    public static final int QUEST_ACCESSIBLE = 1;
    public static final int QUEST_SUCCESS = 2;
    private String id;
    private String title;
    private String version;
    private String description;
    private String header;
    private int numberOfMission;
    private int upvote;
    private String author;
    private int downloader;
    private int achieved;
    private LatLng latLng;
    private ArrayList<Integer> pre;
    private ArrayList<Mission> mission;
    private ArrayList<Comment> comment;

    public Quest(){
        pre = new ArrayList<Integer>();
        mission = new ArrayList<Mission>();
        comment = new ArrayList<Comment>();
    }

    public Quest(String id, String title, String version, String description, String header, String author){
        this.id = id;
        this.title = title;
        this.version = version;
        this.description = description;
        this.header = header;
        this.author = author;
        this.upvote = 0;
        this.downloader = 0;
        this.achieved = 0;
        this.numberOfMission = 0;
        this.latLng = new LatLng();
        pre = new ArrayList<Integer>();
        mission = new ArrayList<Mission>();
        comment = new ArrayList<Comment>();
    }

    public Quest(String id, String title, String version, String description, String header, String author, LatLng latLng){
        this.id = id;
        this.title = title;
        this.version = version;
        this.description = description;
        this.header = header;
        this.author = author;
        this.upvote = 0;
        this.downloader = 0;
        this.achieved = 0;
        this.numberOfMission = 0;
        this.latLng = new LatLng(latLng.getLat(), latLng.getLng());
        pre = new ArrayList<Integer>();
        mission = new ArrayList<Mission>();
        comment = new ArrayList<Comment>();
    }

    public Quest(String id, String title, String version, String description, String header, int rating, String author, int downloader, int achieved, int status, int score, ArrayList<Integer> pre, ArrayList<Mission> mission, ArrayList<Comment> comment) {
        this.id = id;
        this.title = title;
        this.version = version;
        this.description = description;
        this.header = header;
        this.upvote = rating;
        this.author = author;
        this.downloader = downloader;
        this.achieved = achieved;
        this.pre = pre;
        this.mission = mission;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getNumberOfMission() {
        return numberOfMission;
    }

    public void setNumberOfMission(int numberOfMission) {
        this.numberOfMission = numberOfMission;
    }

    public void incMission(){
        this.numberOfMission++;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public ArrayList getPre() {
        return pre;
    }

    public void setPre(ArrayList pre) {
        this.pre = pre;
    }

    public ArrayList<Mission> getMission() {
        return mission;
    }

    public void setMission(ArrayList<Mission> mission) {
        this.mission = mission;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDownloader() {
        return downloader;
    }

    public void setDownloader(int downloader) {
        this.downloader = downloader;
    }

    public int getAchieved() {
        return achieved;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
