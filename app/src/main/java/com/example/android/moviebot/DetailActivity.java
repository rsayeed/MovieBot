package com.example.android.moviebot;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviebot.data.MovieContract;
import com.example.android.moviebot.model.MovieDetails;
import com.example.android.moviebot.model.Review;
import com.example.android.moviebot.model.TrailerItem;
import com.example.android.moviebot.utilities.MovieDBJsonUtils;
import com.example.android.moviebot.utilities.NetworkUtils;
import com.example.android.moviebot.utilities.ReviewAdapter;
import com.example.android.moviebot.utilities.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private MovieDetails mMovieDetails;
    private ArrayList<TrailerItem> mTrailerItemList;
    private Review mReview;

    private Context mContext;

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private TextView mMovieTitleTextBox;
    private ImageView mMoviePosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;
    private TextView mRatingTextView;
    private TextView mSynopsisTextView;
    private TextView mTrailerLabelTextView;
    private Button mLoadMoreButton;
    private TextView mReviewsLabelTextView;
    private ProgressBar mDetailProgressBar;
    private ImageButton mFavoriteBtn;

    private String movieID;
    private boolean isFavoriteMovie;

    private int pageCountForReviews;
    private int stateOfFavBtn = -1;

    private boolean getOnlyReviewData = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.v(TAG, "onSaveInstanceMethod state: " + stateOfFavBtn);

        outState.putInt("stateFavBtn", stateOfFavBtn);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.v(TAG, "onRestoreInstanceMethod state: " + savedInstanceState.getInt("stateFavBtn"));

        stateOfFavBtn = savedInstanceState.getInt("stateFavBtn");

        if (stateOfFavBtn == 0) {

            mFavoriteBtn.setBackgroundResource(R.drawable.ic_star_border_big);
            isFavoriteMovie = false;

        } else if (stateOfFavBtn == 1) {

            mFavoriteBtn.setBackgroundResource(R.drawable.ic_star_border_big_filled);
            isFavoriteMovie = true;
        }

    }

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
        mLoadMoreButton = (Button) findViewById(R.id.loadMoreButton);
        mReviewsLabelTextView = (TextView) findViewById(R.id.textViewReviewLabel);
        mTrailerLabelTextView = (TextView) findViewById((R.id.textViewTrailersLabel));
        mFavoriteBtn = (ImageButton) findViewById(R.id.favoriteBtn);

        // Instantiate objects
        mMovieDetails = new MovieDetails();
        mReview = new Review();

        // Set up recycler views for Trailers and Reviews
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTrailers);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewReviews);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTrailerRecyclerView.setNestedScrollingEnabled(false);
        mReviewRecyclerView.setNestedScrollingEnabled(false);

        // Initialize the adapter and attach it to the RecyclerView for Trailers
        mTrailerAdapter = new TrailerAdapter(this, mTrailerItemList, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        // Initialize the adapter and attach it to the RecyclerView for Reviews
        mReviewAdapter = new ReviewAdapter(this, mReview.getListOfReviewItem());
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        // Initialize pageCount for movie reviews
        pageCountForReviews = 0;

        // Get data saved from Intent
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {

                // Get movie ID from main activity
                movieID = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

                Log.v(TAG, "ON CREATE STATE FAV BTN: " + stateOfFavBtn);

                // Get indicator telling us whether this movie is user's favorite or not
                isFavoriteMovie = intentThatStartedThisActivity.getBooleanExtra(getString(R.string.favMovieIntent), false);

                // Set Favorite button image
                if (isFavoriteMovie) {

                    // Checking for state is unreliable here because onrestore isnt called till after this method completes...

                    Log.v(TAG, "FAVORITE MOVIE SET BUTTON");
                    //  stateOfFavBtn = 1;
                    mFavoriteBtn.setBackgroundResource(R.drawable.ic_star_border_big_filled);

                }

                // Call AsyncTask using movieID to get additional movie details and update views accordingly
                loadMovieData(movieID);
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


            // param[0] = sortID (most popular or top rated
            // param[1] = movieID
            // param[2] = APIkey
            if (params.length == 0) {
                return null;
            }

            if (getOnlyReviewData) {

                getReviewDataOnly(params[1], params[2], ++pageCountForReviews);
            }

            // Get all data for selected movie
            else {

                URL movieInfoRequestUrl =
                        NetworkUtils.buildUrlForMoviePoster(params[0], params[1], params[2]);

                URL trailerInfoRequestUrl =
                        NetworkUtils.buildUrlForMovieTrailer(params[1], params[2]);

                try {
                    // Get list of movies as per sorting criteria
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieInfoRequestUrl);

                    // Get trailer list as per sorting criteria
                    String jsonTrailerResponse = NetworkUtils
                            .getResponseFromHttpUrl(trailerInfoRequestUrl);

                    // Get list of GridItems (contains image URL and Movie ID)
                    mMovieDetails = MovieDBJsonUtils.
                            getMovieDetailStringsFromJson(DetailActivity.this, jsonMovieResponse);

                    // Get list of Trailer items
                    mTrailerItemList = MovieDBJsonUtils.
                            getTrailerDetailStringsFromJson(DetailActivity.this, jsonTrailerResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                // Get review data
                getReviewDataOnly(params[1], params[2], ++pageCountForReviews);

                return mMovieDetails;
            }

            return null;

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

                // Refresh data in recycler view adapters
                if (mTrailerItemList == null || mTrailerItemList.size() == 0) {
                    // Don't display trailers layout if there are none
                    mTrailerLabelTextView.setText(R.string.NoTrailersMsg);
                } else {
                    mTrailerAdapter.swapData(mTrailerItemList);
                }

                // Refresh data in Review recyclerView
                updateReviewUI();

                // Only refresh review UI based on indicator
            } else if (getOnlyReviewData) {

                updateReviewUI();

            } else {

                displayErrorMessage();
            }
        }

        // Grouping all tasks associated to getting Review data
        private void getReviewDataOnly(String movieID, String apiKey, int queryPage) {

            URL reviewRequestUrl =
                    NetworkUtils.buildUrlForMovieReviews(movieID, apiKey, queryPage);

            try {
                // Get reviews for a particular movie
                String jsonReviewResponse = NetworkUtils
                        .getResponseFromHttpUrl(reviewRequestUrl);

                // Get list of Review items
                mReview = MovieDBJsonUtils.
                        getReviewDetailStringsFromJson(DetailActivity.this, jsonReviewResponse);


            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }


    /**
     * This method is responsible for only updating the Review components on the Detail screen
     */
    public void updateReviewUI() {

        if (mReview.getListOfReviewItem() == null || mReview.getListOfReviewItem().size() == 0) {

            // Don't display reviews layout if there are none
            mReviewsLabelTextView.setText(R.string.NoReviewsMsg);
            mLoadMoreButton.setVisibility((View.INVISIBLE));

        } else {
            mReviewAdapter.swapData(mReview.getListOfReviewItem());

            // Hide the Load More Button if there are no more reviews to load
            if (pageCountForReviews == mReview.getTotalReviewPages()) {
                mLoadMoreButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * getMoreReviews is called when the "LOAD MORE REVIEWS" button is clicked.
     * It retrieves additional comments if there are any for selected movie
     */
    public void getMoreReviews(View view) {

        // Determine if this movie has more pages of reviews
        if (pageCountForReviews < mReview.getTotalReviewPages()) {

            getOnlyReviewData = true;
            loadMovieData(movieID);

        }
    }

    /**
     * This method is for responding to clicks to open up trailer links
     */
    @Override
    public void onClick(String trailerKey) {

        Uri trailerLink = NetworkUtils.buildUrlForYoutubeTrailer(trailerKey);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(trailerLink);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + trailerLink.toString() + ", no receiving apps installed!");
        }
    }

    /**
     * This method will handle the logic for adding or deleting a selected movie
     * from user's Favorite's list
     */
    public void onFavoriteButtonClick(View view) {

        // Check if favorite movie is already selected. If so, delete the movie from user's
        // favorite's list and update the Favorite button image accordingly

        if (isFavoriteMovie) {

            // Delete movie from user's favorite button
            mFavoriteBtn.setBackgroundResource(R.drawable.ic_star_border_big);
            isFavoriteMovie = false;

            stateOfFavBtn = 0;

            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(movieID).build();

            getContentResolver().delete(uri, null, null);

            Toast.makeText(this, getString(R.string.FavoritesDeleteDisplayMsg), Toast.LENGTH_SHORT).show();


        } else {

            // Add movie to user's favorite list
            mFavoriteBtn.setBackgroundResource(R.drawable.ic_star_border_big_filled);
            isFavoriteMovie = true;

            stateOfFavBtn = 1;

            // Insert new task data via a ContentResolver
            // Create new empty ContentValues object
            ContentValues contentValues = new ContentValues();

            // Put the movieID into the ContentValues
            contentValues.put(MovieContract.MovieEntry.COLUMN_FAV_MOVIES_ID, movieID);
            // Insert the content values via a ContentResolver
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            // Display the URI that's returned with a Toast
            // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
            if (uri != null) {
                Toast.makeText(this, getString(R.string.FavoritesDisplayMsg), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void displayErrorMessage() {
        Toast.makeText(this, getString(R.string.error_msg), Toast.LENGTH_LONG).show();

    }

}
