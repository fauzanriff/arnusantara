package eu.kudan.rahasianusantara;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fauza on 6/11/2017.
 */

public class LocationController {

    public final static long LOCATION_MIN_TIME = 3000;
    public final static float LOCATION_MIN_DISTANCE = 5.0f;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Context context;

    public LocationController(Context context){
        this.context = context;
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void setListener(LocationListener customListener){
        locationListener = customListener;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_MIN_TIME, LOCATION_MIN_DISTANCE, locationListener);
    }

    public void removeListener(){
        locationManager.removeUpdates(locationListener);
    }
}
