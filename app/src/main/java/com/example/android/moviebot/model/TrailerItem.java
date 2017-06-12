package com.example.android.moviebot.model;

/**
 * Created by rubab on 6/6/17.
 */

public class TrailerItem {

    private String trailerVideoKey;
    private String trailerName;
    private String trailerType;

    public String getTrailerVideoKey() {
        return trailerVideoKey;
    }

    public void setTrailerVideoKey(String trailerVideoKey) {
        this.trailerVideoKey = trailerVideoKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

    @Override
    public String toString() {
        return "TrailerItem{" +
                "trailerVideoKey='" + trailerVideoKey + '\'' +
                ", trailerName='" + trailerName + '\'' +
                ", trailerType='" + trailerType + '\'' +
                '}';
    }
}
