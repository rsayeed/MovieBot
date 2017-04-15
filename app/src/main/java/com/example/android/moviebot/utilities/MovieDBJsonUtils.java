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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class MovieDBJsonUtils {

    private static final String TAG = MovieDBJsonUtils.class.getSimpleName();

    static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p";

    static final String POSTER_SIZE = "w185";

    static final String POSTER_PATH = "poster_path";

    static final String MOVIE_ID = "id";

    static final String TITLE = "original_title";

    static final String RELEASE_DT = "release_date";

    static final String RUNTIME = "runtime";

    static final String RATING = "vote_average";

    static final String OVERVIEW = "overview";


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

    public static MovieDetails getMovieDetailStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String RESULTS_LIST = "results";

        // Declare new MovieDetails object to store the results
        MovieDetails mMovieDetails = new MovieDetails();

        /* Initialize JSONObject with response from server */
        JSONObject movieJson = new JSONObject(movieJsonStr);

        mMovieDetails.setID(movieJson.getString(MOVIE_ID));
        mMovieDetails.setTitle(movieJson.getString(TITLE));
        mMovieDetails.setPosterImage(buildPosterURL(movieJson.getString(POSTER_PATH)));

        String releaseDt = movieJson.getString(RELEASE_DT);
        String yearOfRelease = releaseDt.substring(0,releaseDt.indexOf("-"));
        mMovieDetails.setReleaseDate(yearOfRelease);

        mMovieDetails.setRuntime(movieJson.getString(RUNTIME) + "min");
        mMovieDetails.setRating(movieJson.getString(RATING) + "/10");
        mMovieDetails.setSynopsis(movieJson.getString(OVERVIEW));

        Log.v(TAG, mMovieDetails.toString());

        return mMovieDetails;
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