package eu.kudan.rahasianusantara.component;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import eu.kudan.rahasianusantara.LocationController;
import eu.kudan.rahasianusantara.R;

/**
 * Created by fauza on 6/11/2017.
 */

public class MapFragment extends Fragment{

    public final static long LOCATION_MIN_TIME = 3000;
    public final static float LOCATION_MIN_DISTANCE = 5.0f;

    private MapView mapView;
    private GoogleMap map;
    LocationController locationController;
    eu.kudan.rahasianusantara.model.LatLng currentLocation;
    Location targetLocation;
    String targetTitle;

    private Context context;
    private MarkerOptions markerOptions;
    private Marker markerCurrent;

    public MapFragment(Context context){
        this.context = context;
        markerOptions = new MarkerOptions().position(new LatLng(0,0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        mapView = (MapView) v.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        locationController = new LocationController(context);
        currentLocation = new eu.kudan.rahasianusantara.model.LatLng(-6.914744, 107.609810);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(false);
                map.setTrafficEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                markerCurrent = map.addMarker(markerOptions);
                markerCurrent.setTitle("My Location");
                markerCurrent.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                locationController.setListener(new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        MapsInitializer.initialize(context);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f);
                        map.animateCamera(cameraUpdate);
                        markerCurrent.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        if(targetLocation != null && targetTitle !=null){
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(targetLocation.getLatitude(), targetLocation.getLongitude()))
                                    .title("Quest")
                                    .snippet(targetTitle)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }
                        currentLocation = new eu.kudan.rahasianusantara.model.LatLng(location.getLatitude(), location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        });

        return v;
    }

    public void setDestination(Location target, String title){
        targetLocation = target;
        targetTitle = title;
    }

    public void setDestination(Location target, String title, String snippet){
        map.addMarker(new MarkerOptions()
                .position(new LatLng(target.getLatitude(), target.getLongitude()))
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public eu.kudan.rahasianusantara.model.LatLng getCurrentLocation(){
        return currentLocation;
    }
}
