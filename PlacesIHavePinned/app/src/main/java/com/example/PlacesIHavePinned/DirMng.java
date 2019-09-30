package com.example.PlacesIHavePinned;

import java.io.File;

public class DirMng {
    public static void mkdir(String path){
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
