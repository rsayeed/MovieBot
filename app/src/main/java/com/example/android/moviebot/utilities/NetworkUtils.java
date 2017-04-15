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
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/movie";

    private static final String STATIC_MOVIE_POSTER_URL =
            "http://image.tmdb.org/t/p/";

    final static String API_QUERY_PARAM = "api_key";


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