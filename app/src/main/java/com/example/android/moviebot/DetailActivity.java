package com.example.android.moviebot;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviebot.model.GridItem;
import com.example.android.moviebot.model.MovieDetails;
import com.example.android.moviebot.utilities.MovieDBJsonUtils;
import com.example.android.moviebot.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private MovieDetails mMovieDetails;

    private Context mContext;

    private TextView mMovieTitleTextBox;
    private ImageView mMoviePosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;
    private TextView mRatingTextView;
    private TextView mSynopsisTextView;

    private ProgressBar mDetailProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Change the title of ActionBar to reflect this activity
        getSupportActionBar().setTitle(R.string.ActionBarDetails);

        // Initialize context
        mContext = getApplicationContext();

        // Initialize views
        mMovieTitleTextBox = (TextView) findViewById(R.id.movie_title);
        mMoviePosterImageView = (ImageView) findViewById(R.id.detail_movie_poster);
        mReleaseDateTextView = (TextView) findViewById(R.id.release_date);
        mRuntimeTextView = (TextView) findViewById(R.id.runtime);
        mRatingTextView = (TextView) findViewById(R.id.rating);
        mSynopsisTextView = (TextView) findViewById(R.id.movie_synopsis);
        mDetailProgressBar = (ProgressBar) findViewById(R.id.pb_details_indicator);

        mMovieDetails = new MovieDetails();

        // Get data saved from Intent
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {

                // Call AsyncTask to get movie details and update views accordingly
                loadMovieData(intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT));
            }
        }

    }

    /**
     * This method will load the URL from the NetworkUtils class and execute
     * the AsyncTask to obtain movie data
     */
    private void loadMovieData(String movieID) {

        // Verify that we have internet connection
        if (NetworkUtils.isOnline(getApplicationContext())) {
            Log.v(TAG, "ONLINE");

            new FetchMovieDetailsInfoTask().execute(null, movieID, getString(R.string.API_key));

        } else {
            // No internet connection available
            displayErrorMessage();
        }
    }

    public class FetchMovieDetailsInfoTask extends AsyncTask<String, Void, MovieDetails> {

        @Override
        protected MovieDetails doInBackground(String... params) {


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
                mMovieDetails = MovieDBJsonUtils.
                        getMovieDetailStringsFromJson(DetailActivity.this, jsonMovieResponse);

                return mMovieDetails;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mDetailProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(MovieDetails movieDetail) {

            mDetailProgressBar.setVisibility(View.INVISIBLE);

            if (movieDetail != null) {

                // Set appropriate views here
                mMovieTitleTextBox.setText(movieDetail.getTitle());
                Picasso.with(mContext).load(movieDetail.getPosterImage()).into(mMoviePosterImageView);
                mReleaseDateTextView.setText(movieDetail.getReleaseDate());
                mRuntimeTextView.setText(movieDetail.getRuntime());
                mRatingTextView.setText(movieDetail.getRating());
                mSynopsisTextView.setText(movieDetail.getSynopsis());

            } else {
                displayErrorMessage();
            }
        }
    }

    public void displayErrorMessage() {
        Toast.makeText(this, getString(R.string.error_msg), Toast.LENGTH_LONG).show();

    }

}
