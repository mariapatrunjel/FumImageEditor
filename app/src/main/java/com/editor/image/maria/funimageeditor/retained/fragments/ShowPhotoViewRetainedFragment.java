package com.editor.image.maria.funimageeditor.retained.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;

public class ShowPhotoViewRetainedFragment extends Fragment
{
    // data object we want to retain
    private Bitmap image;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }



}