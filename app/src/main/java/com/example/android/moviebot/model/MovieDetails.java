package com.example.android.moviebot.model;

/**
 * Created by rubab on 4/12/17.
 */

/* Object to store all Movie related details */
public class MovieDetails {

    private String ID;
    private String title;
    private String posterImageURL;
    private String releaseDate;
    private String runtime;
    private String rating;
    private String synopsis;

    public MovieDetails() {}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterImage() {
        return posterImageURL;
    }

    public void setPosterImage(String posterImageURL) {
        this.posterImageURL = posterImageURL;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public String toString() {
        return "MovieDetails{" +
                "ID='" + ID + '\'' +
                ", title='" + title + '\'' +
                ", posterImageURL='" + posterImageURL + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", runtime='" + runtime + '\'' +
                ", rating='" + rating + '\'' +
                ", synopsis='" + synopsis + '\'' +
                '}';
    }
}
