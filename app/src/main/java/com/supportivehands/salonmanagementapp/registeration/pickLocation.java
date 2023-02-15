package com.supportivehands.salonmanagementapp.registeration;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.databinding.ActivityPickLocationBinding;
import com.supportivehands.salonmanagementapp.databinding.ActivityRegisterFormBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class pickLocation extends FragmentActivity {

    private GoogleMap mMap;
    ActivityPickLocationBinding binding;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    Geocoder geocoder;
    List<Address> addressList;
    double selctedLat,selectLng;

    double lastlatitude,lastlogitude;
    ImageButton sendLocation;


    //just for passing data
    String str_name,str_shopname,str_mobile,str_adress,str_city,str_Image;
    String uri;
    String selededAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_location);

        client = LocationServices.getFusedLocationProviderClient(pickLocation.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        getCurrentLocation();

        sendLocation=findViewById(R.id.imageButton);


//        //just getting data, and we will pass it back on buttn click.
        Intent intent=getIntent();
        str_shopname =intent.getStringExtra("shopname");
        str_name=intent.getStringExtra("ownername");
        str_mobile=intent.getStringExtra("mobileNo");
        str_adress=intent.getStringExtra("address");
        str_city=intent.getStringExtra("cityname");
        str_Image=intent.getStringExtra("image");


        Log.d("TAG",str_shopname);
        Log.d("TAG",str_name);
        Log.d("TAG",str_adress);
        Log.d("TAG",str_city);



        //pick location and send to previus activity (also data as well)
        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(pickLocation.this, RegisterForm.class);
                String lat=String.valueOf(lastlatitude);
                String lng=String.valueOf(lastlogitude);
                intent.putExtra("latitude",lat);
                intent.putExtra("logitude",lng);

                intent.putExtra("shopnam",str_shopname);
                intent.putExtra("personname",str_name);
                intent.putExtra("mobile",str_mobile);
                intent.putExtra("address",str_adress);
                intent.putExtra("city",str_city);
                intent.putExtra("mapAdress",selededAdress);
                intent.putExtra("imageBack",str_Image);

                Toast.makeText(pickLocation.this,"Location Picked",Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });


    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap=googleMap;
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Your Current Location");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                            googleMap.addMarker(markerOptions).showInfoWindow();
                           getAdres(location.getLatitude(),location.getLongitude());

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng latLng) {

                                    selctedLat=latLng.latitude;
                                    selectLng=latLng.longitude;
                                    getAdres(selctedLat,selectLng);
                                }
                            });

                        }
                    });

                }


            }
        });
    }
    private void getAdres(double lat,double lng){
        geocoder=new Geocoder(pickLocation.this, Locale.getDefault());
        if (lat != 0){
            try {
                addressList=geocoder.getFromLocation(lat,lng,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null){
                mMap.clear();
                String addres=addressList.get(0).getAddressLine(0);



                String city=addressList.get(0).getLocality();
                String state=addressList.get(0).getAdminArea();
                String country=addressList.get(0).getCountryName();
                String postalcode=addressList.get(0).getPostalCode();
                String area=addressList.get(0).getFeatureName();
                String dis=addressList.get(0).getSubAdminArea();

                 selededAdress=addres;
                if (selededAdress != null){
                    MarkerOptions markerOptions=new MarkerOptions();
                    LatLng latLng=new LatLng(lat,lng);
                    markerOptions.position(latLng).title(selededAdress);
                    // markerOptions.position(latLng).title(area+"-"+city+"-"+dis+"-"+postalcode+"-"+state+"-"+country);
                    mMap.addMarker(markerOptions).showInfoWindow();
                    LatLng position=markerOptions.getPosition();
                    lastlatitude=position.latitude;
                    lastlogitude=position.longitude;
                    Toast.makeText(this,"lat="+position.latitude+",lng="+position.longitude,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"Something went Wrong..!",Toast.LENGTH_SHORT).show();
                }


            }
        }

    }




}