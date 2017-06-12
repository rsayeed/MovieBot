/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.moviebot.utilities;

import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;
import android.util.Log;

import com.example.android.moviebot.R;
import com.example.android.moviebot.model.GridItem;
import com.example.android.moviebot.model.MovieDetails;
import com.example.android.moviebot.model.Review;
import com.example.android.moviebot.model.ReviewItem;
import com.example.android.moviebot.model.TrailerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class MovieDBJsonUtils {

    static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p";
    static final String POSTER_SIZE = "w185";
    static final String POSTER_PATH = "poster_path";
    static final String MOVIE_ID = "id";
    static final String TITLE = "original_title";
    static final String RELEASE_DT = "release_date";
    static final String RUNTIME = "runtime";
    static final String RATING = "vote_average";
    static final String OVERVIEW = "overview";
    private static final String TAG = MovieDBJsonUtils.class.getSimpleName();

    /**
     * This method parses JSON from a web response and returns a list of Grid Items
     * with the URL of the movie poster and the movie ID
     *
     * @param movieJsonStr JSON response from server
     * @return List of GridItems containing image URL and movie ID
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<GridItem> getMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String RESULTS_LIST = "results";

        /* List of GridItems to hold each movie's poster URL and movie ID */
        List<GridItem> parsedGridItemList = new ArrayList<>();

        /* Initialize JSONObject with response from server */
        JSONObject movieJson = new JSONObject(movieJsonStr);

        // Populate array of resuts
        JSONArray results = movieJson.getJSONArray(RESULTS_LIST);

        // Go through each movie result
        for (int x = 0; x < results.length(); x++) {

            JSONObject movies = results.getJSONObject(x);

            GridItem item = new GridItem();

            // Set Poster URL
            item.setImage(buildPosterURL(movies.getString(POSTER_PATH)));

            // Set movie ID
            item.setMovieID(movies.getString(MOVIE_ID));

            Log.v(TAG, item.toString());

            // Add new item to the list
            parsedGridItemList.add(item);

        }

        return parsedGridItemList;
    }

    /**
     * This method parses JSON from a web response and returns a list of Grid Items
     * with the URL of the movie poster and the movie ID
     *
     * @param movieJsonStr List of JSON response from server
     * @return List of GridItems containing image URL and movie ID
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<GridItem> getMovieStringsFromJson(Context context, List<String> movieJsonStr)
            throws JSONException {

        /* List of GridItems to hold each movie's poster URL and movie ID */
        List<GridItem> parsedGridItemList = new ArrayList<>();

        // Loop through list of JSON responses as per user's favorite movies
        for (int i = 0; i < movieJsonStr.size(); i++) {

        /* Initialize JSONObject with response from server */
            JSONObject movieJson = new JSONObject(movieJsonStr.get(i));

            GridItem item = new GridItem();

            // Set Poster URL
            item.setImage(buildPosterURL(movieJson.getString(POSTER_PATH)));

            // Set movie ID
            item.setMovieID(movieJson.getString(MOVIE_ID));

            // Add new item to the list
            parsedGridItemList.add(item);

            Log.v(TAG, item.toString());
        }

        return parsedGridItemList;
    }

    public static MovieDetails getMovieDetailStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        // Declare new MovieDetails object to store the results
        MovieDetails mMovieDetails = new MovieDetails();

        /* Initialize JSONObject with response from server */
        JSONObject movieJson = new JSONObject(movieJsonStr);

        mMovieDetails.setID(movieJson.getString(MOVIE_ID));
        mMovieDetails.setTitle(movieJson.getString(TITLE));
        mMovieDetails.setPosterImage(buildPosterURL(movieJson.getString(POSTER_PATH)));

        String releaseDt = movieJson.getString(RELEASE_DT);
        String yearOfRelease = releaseDt.substring(0, releaseDt.indexOf("-"));
        mMovieDetails.setReleaseDate(yearOfRelease);

        mMovieDetails.setRuntime(movieJson.getString(RUNTIME) + "min");
        mMovieDetails.setRating(movieJson.getString(RATING) + "/10");
        mMovieDetails.setSynopsis(movieJson.getString(OVERVIEW));

        Log.v(TAG, mMovieDetails.toString());

        return mMovieDetails;
    }

    /**
     * This method will parse a JSON string and return the appropriate trailer item object
     *
     * @param context
     * @param movieJsonStr
     * @return
     * @throws JSONException
     */
    public static ArrayList<TrailerItem> getTrailerDetailStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String RESULTS_LIST = "results";

        ArrayList<TrailerItem> myList = new ArrayList<>();

        /* Initialize JSONObject with response from server */
        JSONObject movieJson = new JSONObject(movieJsonStr);

        // Get results array
        JSONArray results = movieJson.getJSONArray(RESULTS_LIST);

        // Loop through array and get
        for (int x = 0; x < results.length(); x++) {

            JSONObject trailerInfo = results.getJSONObject(x);

            // Filter out only trailer types and save in Trailer Item object
            if (trailerInfo.getString("type").equals("Trailer")) {

                TrailerItem trailerItem = new TrailerItem();

                trailerItem.setTrailerVideoKey(trailerInfo.getString("key"));
                trailerItem.setTrailerName(trailerInfo.getString("name"));
                trailerItem.setTrailerType(trailerInfo.getString("type"));

                myList.add(trailerItem);
                Log.v(TAG, trailerItem.toString());

            }
        }

        return myList;
    }

    /**
     * This method will parse a JSON string and return the appropriate review item object
     *
     * @param context
     * @param movieJsonStr
     * @return
     * @throws JSONException
     */
    public static Review getReviewDetailStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String RESULTS_LIST = "results";

        Review movieReview = new Review();

        ArrayList<ReviewItem> myList = new ArrayList<>();

        /* Initialize JSONObject with response from server */
        JSONObject movieJson = new JSONObject(movieJsonStr);

        // Get results array
        JSONArray results = movieJson.getJSONArray(RESULTS_LIST);

        // Pull total # of pages of reviews for this movie
        movieReview.setTotalReviewPages(movieJson.getInt("total_pages"));

        // Loop through array and get
        for (int x = 0; x < results.length(); x++) {

            JSONObject reviewInfo = results.getJSONObject(x);

            // Declare new ReviewItem
            ReviewItem reviewItem = new ReviewItem();

            reviewItem.setAuthor(reviewInfo.getString("author"));
            reviewItem.setReviewDesc(reviewInfo.getString("content"));

            myList.add(reviewItem);
//            Log.v(TAG, "TOTAL COMMENTS: " + myList.size());

        }

        movieReview.setListOfReviewItem(myList);
        Log.v(TAG, movieReview.toString());
        return movieReview;
    }

    /* This method will build out the full path to the movie poster */
    public static String buildPosterURL(String path) {

        String actualPath = path.substring(1);

        Uri builtUri;

        builtUri = Uri.parse(BASE_POSTER_URL)
                .buildUpon()
                .appendPath(POSTER_SIZE)
                .appendPath(actualPath)
                .build();

        return builtUri.toString();
    }

}