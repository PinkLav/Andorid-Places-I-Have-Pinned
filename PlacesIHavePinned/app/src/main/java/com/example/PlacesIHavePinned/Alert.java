package com.example.PlacesIHavePinned;

import android.app.AlertDialog;
import android.content.Context;

public class Alert {
    public static void m(Context c, String msg ) {
        new AlertDialog.Builder(c)
                .setTitle("Error")
                .setMessage(msg)
                .show();
    }
}
