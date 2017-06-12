package com.example.android.moviebot.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.moviebot.DetailActivity;
import com.example.android.moviebot.R;
import com.example.android.moviebot.model.ReviewItem;
import com.example.android.moviebot.model.TrailerItem;

import java.util.ArrayList;

/**
 * Created by rubab on 6/6/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<ReviewItem> mReviewData;
    private Context mContext;

    public ReviewAdapter(Context context, ArrayList<ReviewItem> mReviewData) {
        mContext = context;
        this.mReviewData = mReviewData;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TrailerViewHolder that holds the view for each trailer item
     */
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.reviews_layout, parent, false);

        return new ReviewAdapter.ReviewViewHolder(view);

    }

    /**
     * Called by the RecyclerView to display data at a specified position in the list
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {


        ReviewItem myReviewItem = mReviewData.get(position);
        holder.authorTextView.setText(myReviewItem.getAuthor());
        holder.reviewTextView.setText(myReviewItem.getReviewDesc());

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mReviewData == null) {
            return 0;
        }
        return mReviewData.size();
    }

    /**
     * Update the list of Review Items when new data has been loaded
     */
    public void swapData(ArrayList<ReviewItem> list) {

        if (mReviewData != null && mReviewData.size() > 0)
            mReviewData.addAll(list);

        else {

            mReviewData = list;
        }

        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a particular item
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView authorTextView;
        final TextView reviewTextView;


        ReviewViewHolder(View view) {
            super(view);

            authorTextView = (TextView) view.findViewById(R.id.reviewAuthorTextView);
            reviewTextView = (TextView) view.findViewById(R.id.reviewCommentTextView);
        }

    }
}
