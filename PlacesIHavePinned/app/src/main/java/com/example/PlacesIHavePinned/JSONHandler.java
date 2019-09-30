package com.example.PlacesIHavePinned;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.lang.reflect.Type;
import java.util.ArrayList;



public class JSONHandler {
    public static void toFile(ArrayList<Place> object, String path) {


        Log.i("MAPS", "toFile START");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Log.i("MAPS LEN", "111");

        File f = new File(path);
        f.deleteOnExit();

        Log.i("MAPS LEN", Integer.toString(object.toArray().length));

        try {
            FileWriter writer = new FileWriter(path);
            gson.toJson(object, writer);
            writer.flush();
            writer.close();

            Log.i("MAPS", "CAN WRITE");
        } catch(Exception e){
            Log.i("MAPS", "CANT WRITE");
        }
    }

    public static Object fromFile(String path) {
        Log.i("MAPS", "fromFile START");

        Gson gson = new Gson();
        ArrayList<Place> data = null;

        try {
            final Type REVIEW_TYPE = new TypeToken<ArrayList<Place>>(){}.getType();
            JsonReader reader = new JsonReader(new FileReader(path));
            data = gson.fromJson(reader, REVIEW_TYPE);
            reader.close();

            Log.i("MAPS fromFile", "OK");
        } catch(Exception e){
            Log.i("MAPS fromFile", e.getMessage());
            Log.i("MAPS fromFile",path);
        }

        if(data == null)
        {
            Log.i("MAPS fromFile","NULL");
            data = new ArrayList<>();
        }
        return data;
    }
}
