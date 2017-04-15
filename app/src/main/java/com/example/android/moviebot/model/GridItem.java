package com.example.android.moviebot.model;

/**
 * Created by rubab on 4/9/17.
 */

public class GridItem {

    private String image;
    private String movieID;

    public GridItem () {}

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "GridItem{" +
                "image='" + image + '\'' +
                ", movieID='" + movieID + '\'' +
                '}';
    }
}
