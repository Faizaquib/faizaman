package com.parse.starter;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.security.PublicKey;
import java.util.List;

public class VictimActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    Button EmergencyButton;
    SharedPreferences sharedPreferences;
    Boolean requestActive ;
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            //requestActive=false;
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }



    public  void cancelambulance(View view)
    {

        Toast.makeText(this,"Ambulance canceled",Toast.LENGTH_LONG).show();

        ParseQuery<ParseObject> query = new  ParseQuery<ParseObject>("Request");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size()> 0){
                        for (ParseObject  object : objects){
                            object.deleteInBackground();
                        }
                        requestActive = false;
                        EmergencyButton.setText("Emergency!");
                    }
                }
            }
        });


    }


    public void onEmergency (View view){

       // Toast.makeText(this,Toast.LENGTH_LONG).show();

        if(requestActive){

           // Toast.makeText(this,requestActive.toString()+"first if",Toast.LENGTH_LONG).show();
/*
            ParseQuery<ParseObject> query = new  ParseQuery<ParseObject>("Request");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null){
                        if (objects.size()> 0){
                            for (ParseObject  object : objects){
                                object.deleteInBackground();
                            }
                            requestActive = false;
                            EmergencyButton.setText("Emergency!");
                        }
                    }
                }
            });
*/
        }else {
            //Toast.makeText(this,requestActive.toString()+"else",Toast.LENGTH_LONG).show();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastknowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastknowLocation != null) {

                    ParseUser user = new ParseUser();
                    ParseObject request = new ParseObject("Request");
                    request.put("username", ParseUser.getCurrentUser().getUsername());
                    ParseGeoPoint parseGeoPoint = new ParseGeoPoint(lastknowLocation.getLatitude(), lastknowLocation.getLongitude());
                    request.put("location", parseGeoPoint);
                    request.put("MobileNumber",MainActivity.phoneno);

                    request.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                EmergencyButton.setText("Finding a nearby Ambulance...");
                                requestActive = true;
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       // Toast.makeText(this,requestActive.toString()+"Ambulance canceled",Toast.LENGTH_LONG).show();

                                        ParseQuery<ParseObject> query = new  ParseQuery<ParseObject>("Request");
                                        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                if (e == null){
                                                    if (objects.size()> 0){
                                                        for (ParseObject  object : objects){
                                                            object.deleteInBackground();
                                                        }
                                                        requestActive = false;
                                                        EmergencyButton.setText("Emergency!");
                                                    }
                                                }
                                            }
                                        });

                                    }
                                }, 1200000);
                            }

                        }
                    });


                } else {
                    Toast.makeText(this, "Could not find Location. Make sure to turn your device Location", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastknowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //updateMap(lastknowLocation);
                }
            }
            else
            {

                Toast.makeText(this,"please turn on your location, help us find you.",Toast.LENGTH_SHORT);
                if (Build.VERSION.SDK_INT < 23) {


                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        Location lastknowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (lastknowLocation != null) {
                            updateMap(lastknowLocation);
                        }
                    }

                }

            }
        }
    }

    ;


    public void updateMap(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());


        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        mMap.addMarker(new MarkerOptions().position(userLocation).title("i am here"));


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victim);
        requestActive=false;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        EmergencyButton = (Button) findViewById(R.id.EmergencyButton);

        ParseQuery<ParseObject> query = new  ParseQuery<ParseObject>("Request");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size()> 0){
                        //requestActive = true;
                       // EmergencyButton.setText("Finding a nearby Ambulance...");
                    }
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateMap(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastknowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastknowLocation != null) {
                    updateMap(lastknowLocation);
                }
            }

        }
    }
}

