package com.prueba.becube.becube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    private GoogleMap mMap;
    private static final int MI_ACCESO_UBICACION = 1;
    private LocationManager locationManager;
    private BottomSheetBehavior mBottomSheetBehavior;
    private EditText latitudEditText;
    private EditText longitudEditText;
    private EditText descripcionEditText;
    private Button agregarMarcadorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.app_name));
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(myToolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ubicacion);
        fab.setImageResource(R.drawable.ic_location);
        fab.setOnClickListener(this);

        View bottomSheet = findViewById( R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        latitudEditText = (EditText)findViewById(R.id.latitudEditText);
        longitudEditText = (EditText)findViewById(R.id.longitudEditText);
        descripcionEditText = (EditText)findViewById(R.id.descripcionEditText);
        agregarMarcadorButton = (Button)findViewById(R.id.agregarMarcadorButton);
        agregarMarcadorButton.setOnClickListener(this);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.309306, -99.137728), 15));
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
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab_ubicacion:
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permisoUbicacion();
                } else {
                    ubicacionUsuario();
                }
                break;
            case R.id.agregarMarcadorButton:
                if(validaMarcador()){
                    Toast.makeText(this, getResources().getString(R.string.new_marker), Toast.LENGTH_SHORT).show();
                    LatLng bellasArtes = new LatLng(Float.parseFloat(latitudEditText.getText().toString()), Float.parseFloat(longitudEditText.getText().toString()));
                    mMap.addMarker(new MarkerOptions().position(bellasArtes).title(descripcionEditText.getText().toString()));
                    latitudEditText.setText("");
                    longitudEditText.setText("");
                    descripcionEditText.setText("");
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            default:
                break;
        }
    }

    private boolean validaMarcador(){
        latitudEditText.setError(null);
        longitudEditText.setError(null);
        descripcionEditText.setError(null);
        String latitud = latitudEditText.getText().toString();
        String longitud = longitudEditText.getText().toString();
        String descripcion = descripcionEditText.getText().toString();

        boolean cancelByLatitud = false;
        boolean cancelByLongitud = false;
        boolean cancelByDescripcion = false;

        if (TextUtils.isEmpty(descripcion) && descripcion.trim().length()==0) {
            descripcionEditText.setError(getResources().getString(R.string.invalid_description));
            cancelByDescripcion = true;
        }
        String latitudRegex = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$";
        String longitudRegex = "^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";

        if (!latitud.matches(latitudRegex)) {
            latitudEditText.setError(getResources().getString(R.string.invalid_latitude));
            cancelByLatitud = true;
        }
        if (!longitud.matches(longitudRegex)) {
            longitudEditText.setError(getResources().getString(R.string.invalid_latitude));
            cancelByLongitud = true;
        }
        if (cancelByDescripcion || cancelByLatitud || cancelByLongitud) {
            if (cancelByDescripcion) {
                descripcionEditText.requestFocus();
            }
            if(cancelByLatitud){
                latitudEditText.requestFocus();
            }
            if(cancelByLongitud){
                longitudEditText.requestFocus();
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SharedPreferences prefs = getSharedPreferences("PreferenciasBecube",getApplicationContext().MODE_PRIVATE);
        if(prefs.contains("login") && !prefs.getBoolean("login", false)){
            MenuItem menuItem = menu.getItem(1);
            menuItem.setTitle("Guardar sesión");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                if(mBottomSheetBehavior.isHideable()){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                return true;
            case R.id.settings_item:
                SharedPreferences prefs = getSharedPreferences("PreferenciasBecube",getApplicationContext().MODE_PRIVATE);
                if(prefs.contains("login") && prefs.getBoolean("login", false)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("login", false);
                    editor.apply();
                    Intent intentMainActivity = new Intent(MapsActivity.this, LoginActivity.class);
                    startActivity(intentMainActivity);
                    finish();
                }else{
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("login", true);
                    editor.apply();
                    Intent intentMainActivity = new Intent(MapsActivity.this, LoginActivity.class);
                    startActivity(intentMainActivity);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
