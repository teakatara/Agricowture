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

//    public void setThumbnail(Bitmap thumbnail){
//        mThumbnail = thumbnail;
//    }
//
//    public void setmTitle(String title){
//        mTitle = title;
//    }

    Bitmap getThumbnail(){
        return mThumbnail;
    }

    String getTitle(){
        return mTitle;
    }
}
