package com.example.syouk.Agricowture;

import android.graphics.Bitmap;

public class List_item {
    private Bitmap mThumbnail = null;
    private String mTitle = null;

    public List_item() {}

    List_item(Bitmap thumbnail, String title){
        mThumbnail = thumbnail;
        mTitle = title;
    }

    public void setThumbnail(Bitmap thumbnail){
        mThumbnail = thumbnail;
    }

    public void setmTitle(String title){
        mTitle = title;
    }

    Bitmap getThumbnail(){
        return mThumbnail;
    }

    String getTitle(){
        return mTitle;
    }
}
