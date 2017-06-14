package eu.kudan.rahasianusantara.model;

import java.io.Serializable;

/**
 * Created by fauza on 6/12/2017.
 */

public class LatLng implements Serializable{
    private double lat;
    private double lng;

    public LatLng(){

    }

    public LatLng(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
