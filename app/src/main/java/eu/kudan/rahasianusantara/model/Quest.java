package eu.kudan.rahasianusantara.model;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by fauza on 6/5/2017.
 */

public class Quest {
    public static final int QUEST_FAILED = -1;
    public static final int QUEST_PENDING = 0;
    public static final int QUEST_ACCESSIBLE = 1;
    public static final int QUEST_SUCCESS = 2;
    private int id;
    private String title;
    private String version;
    private String description;
    private Double rating;
    private String author;
    private int downloader;
    private int rater;
    private int finisher;
    private int status;
    private int score;
    private ArrayList<Integer> pre;
    private ArrayList<Mission> mission;
    private ArrayList<Comment> comment;

    public Quest(){
        pre = new ArrayList<Integer>();
        mission = new ArrayList<Mission>();
        comment = new ArrayList<Comment>();
    }

    public Quest(int id, String title, String version, String description, Double rating, ArrayList pre, ArrayList mission, ArrayList comment) {
        this.id = id;
        this.title = title;
        this.version = version;
        this.description = description;
        this.rating = rating;
        this.pre = pre;
        this.mission = mission;
        this.comment = comment;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
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

    public int getRater() {
        return rater;
    }

    public void setRater(int rater) {
        this.rater = rater;
    }

    public int getFinisher() {
        return finisher;
    }

    public void setFinisher(int finisher) {
        this.finisher = finisher;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
