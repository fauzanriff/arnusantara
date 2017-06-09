package eu.kudan.rahasianusantara.model;

/**
 * Created by fauza on 6/8/2017.
 */

public class MissionStatus {
    private int id;
    private int status;

    public MissionStatus(){}

    public MissionStatus(int id, int status) {
        this.id = id;
        this.status = status;
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
}
