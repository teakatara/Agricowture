package com.example.syouk.Agricowture;

import android.graphics.Bitmap;

class List_item {
    private Bitmap mThumbnail;
    private String mTitle;

//    public List_item() {}

    List_item(Bitmap thumbnail, String title){
        mThumbnail = thumbnail;
        mTitle = title;
    }

    Bitmap getThumbnail(){
        return mThumbnail;
    }

    String getTitle(){
        return mTitle;
    }
}
