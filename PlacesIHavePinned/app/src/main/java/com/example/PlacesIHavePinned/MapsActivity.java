package com.example.PlacesIHavePinned;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.CameraUpdate;


import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.io.File;
import java.util.ArrayList;
import java.util.function.Predicate;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<Place> places = null;

    private String placesDir = Environment.getExternalStorageDirectory() + File.separator + "/Download/PlacesIHavePinned/";
    private String placesData = Environment.getExternalStorageDirectory() + File.separator + "/Download/PlacesIHavePinned" + File.separator + "Places.json";

    Marker selectedMarker;

    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        PermissionHandler.Ask(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setMapEvents();
        refreshPlaces();
        updateMap();
    }

    public void updateMap() {

        MarkerMaker markerMaker = new MarkerMaker();

        mMap.clear();

        for (Place p : places) {
            LatLng marker = new LatLng(Double.parseDouble(p.getmLat()), Double.parseDouble(p.getmLng()));

            mMap.addMarker(new MarkerOptions().position(marker)
                        .icon(BitmapDescriptorFactory.fromBitmap(
                            markerMaker.createCustomMarker(MapsActivity.this, p)))
                    .snippet(p.getmId()))
                    .setTitle(p.getmTitle());


            //LatLngBound will cover all your marker on Google Maps
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(marker);

            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
            mMap.moveCamera(cu);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(1), 1, null);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();
        setButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(tag, Integer.toString(resultCode));
        refreshPlaces();
        updateMap();

    }

        private void setButtons(){
            Button btn_add = findViewById(R.id.btn_add_place);

            btn_add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MapsActivity.this, AddPlace.class);
                    //startActivity(i);
                    startActivityForResult(i, 13 );
                }
            });

            Button btn_update = findViewById(R.id.btn_update);

            btn_update.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    refreshPlaces();
                    updateMap();
                }
            });

            btn_delete = findViewById(R.id.btn_delete);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Predicate<Place> condition = p -> p.getmId().equalsIgnoreCase(selectedMarker.getSnippet());

                    places.removeIf(condition);

                    try {
                        JSONHandler.toFile(places, placesData);
                    } catch(Exception e) { }

                    updateMap();
                }
            });
        }

        private void setMapEvents(){
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    refreshPlaces();
                    updateMap();
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    selectedMarker = marker;
                    marker.showInfoWindow();
                    btn_delete.setEnabled(true);
                    return true;
                }
            });

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick (LatLng point) {
                    selectedMarker = null;
                    btn_delete.setEnabled(false);
                }
            });
        }

        private void init(){
            DirMng.mkdir(placesDir);
            File f = new File(placesData);
            if (!f.exists()) {
                ArrayList<Place> pls = new ArrayList();

                try {
                    JSONHandler.toFile(pls, placesData);
                }
                catch (Exception e) {
                    Log.e("RESULT", "ERROR WRITIN");
                }
            }
        }

        private void refreshPlaces(){
            try {
                places = (ArrayList<Place>) JSONHandler.fromFile(placesData);
            }
            catch (Exception e) {
                places = new ArrayList<>();
                Log.i(tag, "ERR READING");
            }


            if(places == null)
                places = new ArrayList<>();
        }

        String tag = "MAPS";
    }

