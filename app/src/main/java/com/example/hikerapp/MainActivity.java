package com.example.hikerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager localManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("Location", location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(@NonNull String s) {

            }

            @Override
            public void onProviderDisabled(@NonNull String s) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            localManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location lastknownLoaction = localManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastknownLoaction != null) {
                updateLocationInfo(lastknownLoaction);
            }

        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            localManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    public void updateLocationInfo(Location location) {
        Log.i("location", location.toString());
        TextView latitude = findViewById(R.id.latitude);
        TextView longitude = findViewById(R.id.longitude);
        TextView accuracy = findViewById(R.id.accuracy);
        TextView altitude = findViewById(R.id.altitude);
        TextView addressView = findViewById(R.id.address);

        latitude.setText("Latitude: "+Double.toString(location.getLatitude()));
        longitude.setText("Longitude: "+Double.toString(location.getLongitude()));
        accuracy.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        altitude.setText("Altitude: "+Double.toString(location.getAltitude()));

        String address="";

        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listAdd=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(listAdd!=null && listAdd.size()>0) {
                if (listAdd.get(0).getThoroughfare() != null) {
                    address += listAdd.get(0).getThoroughfare() + " ";
                }
                if(listAdd.get(0).getLocality()!=null)
                {
                    address+=listAdd.get(0).getLocality()+" ";
                }
                if(listAdd.get(0).getPostalCode()!=null)
                {
                    address+=listAdd.get(0).getPostalCode()+" ";
                }
                if(listAdd.get(0).getAdminArea()!=null)
                {
                    address+=listAdd.get(0).getAdminArea()+" ";
                }
                Log.i("Address",address.toString());
                //Toast.makeText(MainActivity.this,address,Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(address!=null)
               addressView.setText(address);
        else
            addressView.setText("Could not find address!!");
      }
  }