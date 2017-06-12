package com.example.android.moviebot;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moviebot.data.MovieContract;
import com.example.android.moviebot.model.GridItem;
import com.example.android.moviebot.utilities.GridViewAdapter;
import com.example.android.moviebot.utilities.MovieDBJsonUtils;
import com.example.android.moviebot.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;

    // Store user's list of movies
    private Set<String> mFavoriteMovieSet;

    private static final int MOVIES_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Change the title of ActionBar to reflect this activity
        getSupportActionBar().setTitle(R.string.ActionBarPopular);

        // Initialize GridView and ProgressBar
        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_view, mGridData);
        mGridView.setAdapter(mGridAdapter);

        // Initialize HashSet for user's movie collection
        mFavoriteMovieSet = new HashSet<>();

        // Set up an onClick event to the GridView
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, item.getMovieID());

                Log.v(TAG, "MOVIE SELECTED IS: " + item.getMovieID());

                // Check list of user's favorite movies if selected movie is a favorite or not
                intent.putExtra(getString(R.string.favMovieIntent), mFavoriteMovieSet.contains(item.getMovieID()));

                //Start details activity
                startActivity(intent);
            }
        });

        // Once all of our views are setup, we can load the movie data using default sort
        // parameter (popular)
        loadMovieData(null);


        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used. This loader will be used to
         query for a user's movies
         */
        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);

    }

    /**
     * This method will load the URL from the NetworkUtils class and execute
     * the AsyncTask to obtain movie data
     */
    private void loadMovieData(String sortOrder) {

        /* Default sort order is popular */
        if (sortOrder == null) {

            sortOrder = getString(R.string.popular);
        }

        // Verify that we have internet connection
        if (NetworkUtils.isOnline(getApplicationContext())) {
            Log.v(TAG, "ONLINE");

            new FetchMovieInfoTask().execute(sortOrder, null, getString(R.string.API_key));

        } else {
            // No internet connection available
            displayErrorMessage();

        }

    }


    public class FetchMovieInfoTask extends AsyncTask<String, Void, List<GridItem>> {

        @Override
        protected List<GridItem> doInBackground(String... params) {

            // param[0] = sortID (most popular or top rated
            // param[1] = movieID
            // param[2] = APIkey
            if (params.length == 0) {
                return null;
            }

            URL movieInfoRequestUrl;
            List<GridItem> movieDataList;

            // If request is to display user's favorite movies
            if (params[0].equals(getString(R.string.actionFavorite))) {

                Log.v(TAG, "processing Favorite request");
                movieDataList = loadFavoriteMovies(mFavoriteMovieSet, params[2]);
            }

            // Load general movies list as per sorting criteria
            else {
                movieInfoRequestUrl =
                        NetworkUtils.buildUrlForMoviePoster(params[0], params[1], params[2]);

                try {

                    // Get list of movies as per sorting criteria
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieInfoRequestUrl);

                    // Get list of GridItems (contains image URL and Movie ID)
                    movieDataList = MovieDBJsonUtils.
                            getMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            return movieDataList;
        }

        @Override
        protected void onPreExecute() {

            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(List<GridItem> gridData) {

            mProgressBar.setVisibility(View.INVISIBLE);

            if (gridData != null) {
                mGridAdapter.setGridData(gridData);
            } else {
                displayErrorMessage();
            }
        }

        // Method to handle the display of Favorite movies list
        private List<GridItem> loadFavoriteMovies(Set<String> favoriteMoviesSet, String apiKey) {

            List<GridItem> favGridItem;

            ArrayList<URL> listOfFavoriteMoviesUrl = NetworkUtils
                    .buildUrlForMoviePoster(favoriteMoviesSet, apiKey);

            try {

                // Get list of movies as per sorting criteria
                List<String> jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(listOfFavoriteMoviesUrl);

                // Get list of GridItems (contains image URL and Movie ID)
                favGridItem = MovieDBJsonUtils.
                        getMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return favGridItem;
        }
    }

    public void displayErrorMessage() {
        Toast.makeText(this, getString(R.string.error_msg), Toast.LENGTH_LONG).show();

    }

    /* Inflate the menu for the application */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_settings, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /* Add behavior to the options that are found in the application menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_popular:
                mGridAdapter.setGridData(null); // null will load default option (popular)
                getSupportActionBar().setTitle(R.string.ActionBarPopular);
                loadMovieData(null);
                return true;

            case R.id.action_top_rated:
                mGridAdapter.setGridData(null);
                getSupportActionBar().setTitle(R.string.menu_toprated_title);
                loadMovieData(getString(R.string.topRated));
                return true;

            case R.id.action_favorites:

                if (mFavoriteMovieSet != null && mFavoriteMovieSet.size() > 0) {
                    mGridAdapter.setGridData(null);
                    getSupportActionBar().setTitle(R.string.menu_favorites);
                    loadMovieData(getString(R.string.actionFavorite));
                } else {

                    // Display a Toast notifying user that they do not have any Favorite movies
                    Toast.makeText(this, getString(R.string.favoritesNotFound), Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted from DetailActivity
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
    }


    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     * <p>
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data
                Log.v(TAG, "QUERYING FOR NEW FAVORITES");
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {

                mMovieData = data;
                super.deliverResult(data);
            }
        };

    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Populate a HashSet with the user's favorite movies

        if (data != null) {

            if (data.moveToFirst()) {

                // loop through cursor and store the results in the hashset
                do {
                    // Only one column in the database to store movie ID
                    mFavoriteMovieSet.add(String.valueOf(data.getInt(0)));

                    Log.v(TAG, "Added movie ID: " + String.valueOf(data.getInt(0)));

                } while (data.moveToNext());

            }

        }

        Log.v(TAG, "size of movie list: " + mFavoriteMovieSet.size());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteMovieSet = null;
    }
}
