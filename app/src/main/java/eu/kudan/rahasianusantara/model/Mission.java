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
    public static final int MODEL_3D = 0;
    public static final int MODEL_IMAGE = 1;
    public static final int MODEL_VIDEO = 2;
    public static final int BASED_MARKER = 1;
    public static final int BASED_MARKERLESS = 0;
    private String id;
    private String title;
    private int order;
    private LatLng latLng;
    private String modelLink;
    private String markerLink;
    private int modelType;
    private int arBased;
    private String dialogue;

    public Mission(){}

    public Mission(String id, String title, int order, int modelType, String modelLink, String markerLink, int arBased, LatLng latLng, String dialogue) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.modelType = modelType;
        this.modelLink = modelLink;
        this.arBased = arBased;
        this.markerLink = markerLink;
        this.latLng = latLng;
        this.dialogue = dialogue;
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

    public String getModelLink() {
        return modelLink;
    }

    public void setModelLink(String modelLink) {
        this.modelLink = modelLink;
    }

    public String getMarkerLink() {
        return markerLink;
    }

    public void setMarkerLink(String markerLink) {
        this.markerLink = markerLink;
    }

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

}
