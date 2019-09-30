package com.example.PlacesIHavePinned;

import java.io.Serializable;

public class Place implements Serializable {

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLng() {
        return mLng;
    }

    public void setmLng(String mLng) {
        this.mLng = mLng;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) { this.mImage = mImage; }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) { this.mId = mId; }

    private String mTitle = "NO TITLE";
    private String mLat = "0";
    private String mLng = "0";
    private String mImage;
    private String mId = Long.toString(java.lang.System.currentTimeMillis());

}
