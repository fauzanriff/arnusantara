package eu.kudan.rahasianusantara.model;

import java.util.ArrayList;

/**
 * Created by fauza on 6/8/2017.
 */

public class QuestStatus {
    private int id;
    private int status;
    private int vote;
    private int score;
    private int orderStatus;
    private ArrayList<MissionStatus> missionStatus;

    public QuestStatus (){}

    public QuestStatus(int id, int status, int vote, int score) {
        this.id = id;
        this.status = status;
        this.vote = vote;
        this.score = score;
    }

    public QuestStatus(int id, int status, int vote, int score, ArrayList<MissionStatus> missionStatus) {
        this.id = id;
        this.status = status;
        this.vote = vote;
        this.score = score;
        this.missionStatus = missionStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
