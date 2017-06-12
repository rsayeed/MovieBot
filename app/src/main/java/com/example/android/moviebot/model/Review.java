package com.example.android.moviebot.model;

import java.util.ArrayList;

/**
 * Created by rubab on 6/8/17.
 */

public class Review {

    private ArrayList<ReviewItem> listOfReviewItem;
    int totalReviewPages;

    public ArrayList<ReviewItem> getListOfReviewItem() {
        return listOfReviewItem;
    }

    public void setListOfReviewItem(ArrayList<ReviewItem> listOfReviewItem) {
        this.listOfReviewItem = listOfReviewItem;
    }

    public int getTotalReviewPages() {
        return totalReviewPages;
    }

    public void setTotalReviewPages(int totalReviewPages) {
        this.totalReviewPages = totalReviewPages;
    }

    @Override
    public String toString() {
        return "Review{" +
                "totalReviewPages=" + totalReviewPages +
                '}';
    }
}
