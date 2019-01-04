package com.prueba.becube.becube;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    private GoogleMap mMap;
    private static final int MI_ACCESO_UBICACION = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ubicacion);
        fab.setImageResource(R.drawable.ic_location);
        fab.setOnClickListener(this);
    }

    private void ubicacionUsuario(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permisoUbicacion();
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location selfLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if(selfLocation == null){
            Criteria locationCriteria = new Criteria();
            locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
            locationManager.requestLocationUpdates(locationManager.getBestProvider(locationCriteria, true), 1000, 10, this);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 10, this);
            }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000, 10, this);
            }else{
                Toast.makeText(this, getResources().getString(R.string.gps_disabled), Toast.LENGTH_SHORT).show();
            }
        }else{
            if(mMap != null){
                LatLng selfLoc;
                CameraUpdate update;
                selfLoc = new LatLng(selfLocation.getLatitude(), selfLocation.getLongitude());
                update = CameraUpdateFactory.newLatLngZoom(selfLoc, 15);
                mMap.animateCamera(update);
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private void permisoUbicacion() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                MI_ACCESO_UBICACION);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng bellasArtes = new LatLng(19.4352, -99.1412);
        mMap.addMarker(new MarkerOptions().position(bellasArtes).title("Palacio de Bellas Artes"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.309306, -99.137728), 10));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.json_map_style));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permisoUbicacion();
        }else{
            ubicacionUsuario();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permisoUbicacion();
        }
        if(mMap != null){
            LatLng selfLoc;
            CameraUpdate update;
            selfLoc = new LatLng(location.getLatitude(), location.getLongitude());
            update = CameraUpdateFactory.newLatLngZoom(selfLoc, 15);
            mMap.animateCamera(update);
            mMap.setMyLocationEnabled(true);
        }
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab_ubicacion) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permisoUbicacion();
            } else {
                ubicacionUsuario();
            }
        }
    }
}
