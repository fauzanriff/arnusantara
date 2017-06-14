package eu.kudan.rahasianusantara.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fauza on 6/5/2017.
 */

public class Mission implements Serializable {
    public static final int MISSION_FAILED = -1;
    public static final int MISSION_PENDING = 0;
    public static final int MISSION_ACCESSIBLE = 1;
    public static final int MISSION_SUCCESS = 2;
    public static final int MODEL_3D = 1;
    public static final int MODEL_IMAGE = 2;
    public static final int MODEL_VIDEO = 3;
    public static final int BASED_MARKER = 1;
    public static final int BASED_MARKERLESS = 1;
    private int id;
    private String title;
    private int order;
    private LatLng latLng;
    private int modelType;
    private int arBased;
    private ArrayList<String> dialogue;

    public Mission(){
        dialogue = new ArrayList<String>();
    }

    public Mission(int id, String title, int order, int modelType, int arBased, LatLng latLng, ArrayList<String> dialogue) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.modelType = modelType;
        this.arBased = arBased;
        this.latLng = latLng;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public int getArBased() {
        return arBased;
    }

    public void setArBased(int arBased) {
        this.arBased = arBased;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public ArrayList<String> getDialogue() {
        return dialogue;
    }

    public void setDialogue(ArrayList<String> dialogue) {
        this.dialogue = dialogue;
    }
}
