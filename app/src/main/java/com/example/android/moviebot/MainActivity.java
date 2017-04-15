package com.example.android.moviebot;

import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
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

import com.example.android.moviebot.R;
import com.example.android.moviebot.model.GridItem;
import com.example.android.moviebot.utilities.GridViewAdapter;
import com.example.android.moviebot.utilities.MovieDBJsonUtils;
import com.example.android.moviebot.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;

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

        // Set up an onClick event to the GridView
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, item.getMovieID());

                //Start details activity
                startActivity(intent);
            }
        });

        // Once all of our views are setup, we can load the movie data using default sort
        // parameter (popular)
        loadMovieData(null);

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

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            URL movieInfoRequestUrl =
                    NetworkUtils.buildUrlForMoviePoster(params[0], params[1], params[2]);

            try {

                // Get list of movies as per sorting criteria
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieInfoRequestUrl);

                // Get list of GridItems (contains image URL and Movie ID)
                List<GridItem> movieDataList = MovieDBJsonUtils.
                        getMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return movieDataList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
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
                loadMovieData(null);
                return true;

            case R.id.action_top_rated:
                mGridAdapter.setGridData(null);
                loadMovieData(getString(R.string.topRated));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
