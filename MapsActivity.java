package com.ensure.vac;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ensure.vac.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Double addressLatitude = 33.68669319492311;
    private Double addressLongitude = 73.05098176002502;
    private Button btnSave;
    private ImageView pinButton;
    private String isFrom;
    private ArrayList<GetLocationModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pinButton = (ImageView) findViewById(R.id.pin);
        btnSave = findViewById(R.id.btnSave);
        findViewById(R.id.ivBack).setOnClickListener(view -> {
            finish();
        });
        getIntentData();
        arrayList = new ArrayList<>();

        btnSave.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("ADDRESS", getAddressInfo(addressLatitude, addressLongitude));
            intent.putExtra("LATITUDE", addressLatitude.toString());
            intent.putExtra("LONGITUDE", addressLongitude.toString());
            setResult(100, intent);
            finish();
        });
    }

    private String getAddressInfo(Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String address = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);

            Log.e("address :", address);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    /*private void getIntentData() {
        String address = getIntent().getStringExtra("Area1");
        Log.e("address :", String.valueOf(getLatLagFromAddress(address)));

        addressLatitude = getLatLagFromAddress(address).latitude;
        addressLongitude = getLatLagFromAddress(address).longitude;

        Log.e("address :", String.valueOf(addressLatitude));
        Log.e("address :", String.valueOf(addressLongitude));
    }*/

    public LatLng getLatLagFromAddress(String strAddress){
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address == null) {
                return new LatLng(-10000, -10000);
            }
            Address location = address.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            return new LatLng(-10000, -10000);
        }
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

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        if (isFrom.equals("TEAM")) {
            pinButton.setVisibility(View.GONE);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child("Teams");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        if (dataSnapshot.child("addWorkerId")
                                .getValue().toString().contains(firebaseUser.getUid())) {
                            addressLatitude = Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                            addressLongitude = Double.valueOf(dataSnapshot.child("longitude").getValue().toString());

                            arrayList.add(new GetLocationModel(
                                    addressLatitude,
                                    addressLongitude,
                                    dataSnapshot.child("teamName").getValue().toString()
                            ));
                        }
                    }

                    if (!arrayList.isEmpty()) {
                        for (GetLocationModel getLocationModel : arrayList) {
                            LatLng address = new LatLng(
                                    getLocationModel.latitude, getLocationModel.longitude
                            );
                            mMap.addMarker(new MarkerOptions().position(address).title(
                                    getLocationModel.teamName
                            ));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(getLocationModel.latitude, getLocationModel.longitude), 12f)
                            );
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (isFrom.equals("AdminHome")) {
            pinButton.setVisibility(View.GONE);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child("Teams");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        addressLatitude = Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                        addressLongitude = Double.valueOf(dataSnapshot.child("longitude").getValue().toString());

                        arrayList.add(new GetLocationModel(
                            addressLatitude,
                            addressLongitude,
                            dataSnapshot.child("teamName").getValue().toString()
                        ));
                    }

                    if (!arrayList.isEmpty()) {
                        for (GetLocationModel getLocationModel : arrayList) {
                            LatLng address = new LatLng(
                                    getLocationModel.latitude, getLocationModel.longitude
                            );
                            mMap.addMarker(new MarkerOptions().position(address).title(
                                    getLocationModel.teamName
                            ));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(getLocationModel.latitude, getLocationModel.longitude), 12f)
                            );
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            LatLng address = new LatLng(addressLatitude, addressLongitude);
//        mMap.addMarker(new MarkerOptions().position(address).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(addressLatitude, addressLongitude), 12f)
            );

        /*LatLng address1 = new LatLng(54.15262562824432, 51.983706839382656);
        mMap.addMarker(new MarkerOptions().position(address1).title("Marker1"));

        LatLng address2 = new LatLng(43.526924315533, 67.42445357143879);
        mMap.addMarker(new MarkerOptions().position(address2).title("Marker2"));

        LatLng address3 = new LatLng(49.13800429335616, 111.79302334785463);
        mMap.addMarker(new MarkerOptions().position(address3).title("Marker3"));
*/

            googleMap.setOnCameraIdleListener(() -> {
                Log.e("address :", String.valueOf(googleMap.getCameraPosition().target.latitude));
                Log.e("address :", String.valueOf(googleMap.getCameraPosition().target.longitude));

                addressLatitude = googleMap.getCameraPosition().target.latitude;
                addressLongitude = googleMap.getCameraPosition().target.longitude;

                if (btnSave.getVisibility() == View.GONE) {
                    btnSave.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void getIntentData() {
        isFrom =  getIntent().getStringExtra("IS_FROM_KEY");
    }
}

