package com.hellven.net;

import android.graphics.drawable.Drawable;

/**
 * Created by PC on 2017-06-04.
 */

public class List_Item {
    public String title;
    public String artist;
    public Drawable Image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Drawable getImage() {
        return Image;
    }

    public void setImage(Drawable image) {
        Image = image;
    }
}
