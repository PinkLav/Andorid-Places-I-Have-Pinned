package com.example.PlacesIHavePinned;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.location.Location;

import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.content.Intent;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AddPlace extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 13;

    private Bitmap img;
    private String dir = Environment.getExternalStorageDirectory() + File.separator + "Download/PlacesIHavePinned/";
    private String placesData = Environment.getExternalStorageDirectory() + File.separator + "Download/PlacesIHavePinned" + File.separator + "Places.json";
    final Place p = new Place();

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        dispatchTakePictureIntent();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AddPlace.this);
        getLocation();

        Button b = findViewById(R.id.btn_add);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = findViewById(R.id.title);
                String title = edit.getText().toString();

                saveImage(img, dir, title);

                p.setmTitle(title);
                p.setmImage(dir + title + ".jpg");


                try {
                        ArrayList<Place> data = (ArrayList<Place>) JSONHandler.fromFile(placesData);
                        Log.i("ADD", "READ");
                        data.add(p);
                        JSONHandler.toFile(data, placesData);
                        Log.i("ADD", "WRITE");


                    AddPlace.super.finish();
                    } catch (Exception e) {
                        Alert.m(AddPlace.this ,"Adding data to file " + placesData);
                    }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            img = (Bitmap) extras.get("data");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void saveImage(Bitmap image,String dir,String name){
        File file = new File (dir, name+".jpg");
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocation(){
        Task locationResult = mFusedLocationClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {

                    // Set the map's camera position to the current location of the device.
                    Location mLastKnownLocation = (Location) task.getResult();
                    p.setmLat(Double.toString(mLastKnownLocation.getLatitude()));
                    p.setmLng(Double.toString(mLastKnownLocation.getLongitude()));


                } else {
                    Log.d("ADDPLACE", "Current location is null. Using defaults.");
                    Log.e("ADDPLACE", "Exception: %s", task.getException());

                    new AlertDialog.Builder(AddPlace.this)
                            .setTitle("Error")
                            .setMessage("Current location is null.")
                            .show();
                }
            }
        });
    }
}
