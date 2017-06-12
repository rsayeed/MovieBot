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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/movie";

    private static final String STATIC_MOVIE_POSTER_URL =
            "http://image.tmdb.org/t/p/";

    private static final String STATIC_TRAILER_BASE_URL =
            "http://www.youtube.com/watch";

    final static String API_QUERY_PARAM = "api_key";
    final static String VIDEO_QUERY_PARAM = "videos";
    final static String REVIEW_QUERY_PARAM = "reviews";
    final static String PAGE_QUERY_PARAM = "page";
    final static String YOUTUBE_QUERY_PARAM = "v";


    /**
     * This method will construct the URL to go against the movieDB API and return
     * appropriate details as per the parameters passed in from MainActivity
     *
     * @param sortKey
     * @param movieID
     * @return
     */
    public static URL buildUrlForMoviePoster(String sortKey, String movieID, String APIKey) {

        Uri builtUri;

        if (movieID != null) {

            // Build URL to retrieve movie Details
            builtUri = Uri.parse(STATIC_MOVIE_BASE_URL)
                    .buildUpon()
                    .appendPath(movieID)
                    .appendQueryParameter(API_QUERY_PARAM, APIKey)
                    .build();
        }

        // Build the URL as per the sortKey requested
        else {
            builtUri = Uri.parse(STATIC_MOVIE_BASE_URL)
                    .buildUpon()
                    .appendPath(sortKey)
                    .appendQueryParameter(API_QUERY_PARAM, APIKey)
                    .build();
        }


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method will construct the URL to go against the movieDB API and return
     * appropriate details as per the parameters passed in from MainActivity
     *
     * @return
     */
    public static ArrayList<URL> buildUrlForMoviePoster(Set<String> favoriteMoviesList, String APIKey) {

        Uri builtUri;
        ArrayList<URL> urlList = new ArrayList<>();

        Iterator<String> setIterator = favoriteMoviesList.iterator();

        // Loop through Set elements containing Movie ID's
        while(setIterator.hasNext()) {

            // Build URL to retrieve movie Details
            builtUri = Uri.parse(STATIC_MOVIE_BASE_URL)
                    .buildUpon()
                    .appendPath(setIterator.next())
                    .appendQueryParameter(API_QUERY_PARAM, APIKey)
                    .build();


            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            urlList.add(url);

            Log.v(TAG, "Built URI " + url);
        }

        return urlList;
    }

    /**
     * This method will construct the URL to go against the movieDB API and return
     * appropriate details pertaining to Trailer video information
     *
     */
    public static URL buildUrlForMovieTrailer(String movieID, String APIKey) {

        Uri builtUri;

        // Build the URL to retreive trailers
        builtUri = Uri.parse(STATIC_MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(movieID)
                .appendPath(VIDEO_QUERY_PARAM)
                .appendQueryParameter(API_QUERY_PARAM, APIKey)
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method will construct the URL to go against the movieDB API and return
     * Reviews for a given movie
     *
     */
    public static URL buildUrlForMovieReviews(String movieID, String APIKey, int pageCount) {

        Uri builtUri;

        // Build the URL to retreive trailers
        builtUri = Uri.parse(STATIC_MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(movieID)
                .appendPath(REVIEW_QUERY_PARAM)
                .appendQueryParameter(API_QUERY_PARAM, APIKey)
                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(pageCount))
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method will construct the URI to view the youtube trailer
     *
     */
    public static Uri buildUrlForYoutubeTrailer(String videoID) {

        Uri builtUri;

        // Build the URL to retreive trailers
        builtUri = Uri.parse(STATIC_TRAILER_BASE_URL)
                .buildUpon()
                .appendQueryParameter(YOUTUBE_QUERY_PARAM, videoID)
                .build();

        Log.v(TAG, "Built URI " + builtUri);

        return builtUri;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {

                String movieData = scanner.next();
                Log.v(TAG, "Movie Data: " + movieData);
                return movieData;

            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param urlList The list of URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static List<String> getResponseFromHttpUrl(List<URL> urlList) throws IOException {

        List<String> httpRespList = new ArrayList<>();

        for (int i = 0; i < urlList.size(); i++) {

            HttpURLConnection urlConnection = (HttpURLConnection) urlList.get(i).openConnection();

            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {

                    String movieData = scanner.next();
                    Log.v(TAG, "Favorite Movie Data: " + movieData);
                    httpRespList.add(movieData);

                } else {
                    return null;
                }

            } finally {
                urlConnection.disconnect();
            }
        }

       return httpRespList;
    }


    /**
     * This method will verify that the device has proper Internet connection. It will
     * try and ping Google's DNS to and wait for a successful response
     *
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}