package com.example.PlacesIHavePinned;

import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;

public class PermissionHandler {
    static int MY_PERMISSIONS = 13;

    public static void Ask(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA},
                MY_PERMISSIONS);
    }
}
