package com.example.android.moviebot.data;

import android.net.Uri;

/**
 * Created by rubab on 6/9/17.
 */

public class MovieContract {

      /* Add content provider constants to the Contract
     Clients need to know how to access the task data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
      */

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.android.moviebot";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "movies" directory
    public static final String MOVIE_TASKS = "movies";

    /* MovieEntry is an inner class that defines the contents of the Movie table */
    public static final class MovieEntry {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_TASKS).build();


        // Movie table and column names
        public static final String TABLE_NAME = "movies";

        // Since MovieEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column
        public static final String COLUMN_FAV_MOVIES_ID = "FAVORITE_MOVIE_ID";


    }
}
