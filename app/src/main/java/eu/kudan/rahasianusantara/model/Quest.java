package eu.kudan.rahasianusantara.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import eu.kudan.rahasianusantara.R;

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
    private int upvote;
    private String author;
    private int downloader;
    private int achieved;
    private LatLng latLng;
    private ArrayList<Integer> pre;
    private ArrayList<Mission> missions;
    private ArrayList<Comment> comment;

    public Quest(){
        pre = new ArrayList<Integer>();
        missions = new ArrayList<Mission>();
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
        this.latLng = new LatLng();
        pre = new ArrayList<Integer>();
        missions = new ArrayList<Mission>();
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
        this.latLng = new LatLng(latLng.getLat(), latLng.getLng());
        pre = new ArrayList<Integer>();
        missions = new ArrayList<Mission>();
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
        this.missions = mission;
        this.comment = comment;
    }

    public Quest(Quest q){
        this.id = q.getId();
        this.title = q.getTitle();
        this.version = q.getVersion();
        this.description = q.getDescription();
        this.header = q.getHeader();
        this.author = q.getAuthor();
        this.upvote = q.getUpvote();
        this.downloader = q.getDownloader();
        this.achieved = q.getAchieved();
        this.latLng = q.getLatLng();
        pre = new ArrayList<Integer>();
        missions = new ArrayList<Mission>();
        comment = new ArrayList<Comment>();
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
        return missions;
    }

    public void setMission(ArrayList<Mission> mission) {
        this.missions = mission;
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

    public static void saveQuest(Quest q, Context context){
        String fileName = "/quest_"+q.getId();

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(), fileName)));
            os.writeObject(q);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveActiveQuest(Quest q, Context context){
        String fileName = "/activequest";

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(), fileName)));
            os.writeObject(q);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Quest loadData(String id, Context context){
        Quest output = new Quest();
        String fileName = "/quest_"+id;

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(context.getCacheDir(), fileName)));
            output = (Quest) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static Quest loadActiveQuest(Context context){
        Quest output = new Quest();
        String fileName = "/activequest";

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(context.getCacheDir(), fileName)));
            output = (Quest) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static boolean availableOffline(String id, Context context){
        String fileName = "/quest_"+id;
        File file = new File(context.getCacheDir(), fileName);
        Log.d("json", file.toString());
        return (file.exists() && !file.isDirectory());
    }

    public static boolean activeAvailable(Context context){
        String fileName = "/activequest";

        File file = new File(context.getCacheDir(), fileName);
        return (file.exists() && !file.isDirectory());
    }

    public static void deleteActiveQuest(Context context){
        String fileName = "/activequest";

        File file = new File(context.getCacheDir(), fileName);
        file.delete();
    }
}
