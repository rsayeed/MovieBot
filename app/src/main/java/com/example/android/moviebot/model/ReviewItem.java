package com.example.android.moviebot.model;

/**
 * Created by rubab on 6/6/17.
 */

public class ReviewItem {

    String author;
    String reviewDesc;

    public ReviewItem() { }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewDesc() {
        return reviewDesc;
    }

    public void setReviewDesc(String reviewDesc) {
        this.reviewDesc = reviewDesc;
    }

    @Override
    public String toString() {
        return "ReviewItem{" +
                "author='" + author + '\'' +
                ", reviewDesc='" + reviewDesc + '\'' +
                '}';
    }
}
