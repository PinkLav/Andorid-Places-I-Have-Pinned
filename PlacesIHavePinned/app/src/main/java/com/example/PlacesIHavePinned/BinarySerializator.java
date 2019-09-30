package com.example.PlacesIHavePinned;

import java.io.IOException;

import java.io.File;

import 	java.io.ObjectOutputStream;
import 	java.io.ObjectInputStream;

import 	java.io.FileOutputStream;
import 	java.io.FileInputStream;


// BINARY SERIALIZATION
public class BinarySerializator {
    public static void toFile(Object object, String path) throws IOException {

        File data = new File(path);
        if (!data.createNewFile()) {
            data.delete();
            data.createNewFile();
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(data));
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
    }

    public static Object fromFile(String path) throws IOException, ClassNotFoundException {
        Object object = null;
        File data = new File(path);
        if(data.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(data));
            object = objectInputStream.readObject();
            objectInputStream.close();
        }
        return object;
    }
}