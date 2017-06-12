package com.example.android.moviebot.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviebot.DetailActivity;
import com.example.android.moviebot.R;
import com.example.android.moviebot.model.TrailerItem;

import java.util.ArrayList;

/**
 * Created by rubab on 6/6/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private ArrayList<TrailerItem> mTrailerData;
    private Context mContext;

    /*
  * Below, we've defined an interface to handle clicks on items within this Adapter. In the
  * constructor of our TrailerAdapter, we receive an instance of a class that has implemented
  * said interface. We store that instance in this variable to call the onClick method whenever
  * an item is clicked in the list.
  */
    final private TrailerAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailerKey);
    }

    public TrailerAdapter(Context context, ArrayList<TrailerItem> mTrailerData, TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        this.mTrailerData = mTrailerData;
        mClickHandler = clickHandler;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TrailerViewHolder that holds the view for each trailer item
     */
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.trailers_layout, parent, false);

        return new TrailerAdapter.TrailerViewHolder(view);

    }

    /**
     * Called by the RecyclerView to display data at a specified position in the list
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {

        TrailerItem myTrailerItem = mTrailerData.get(position);
        holder.trailerName.setText(myTrailerItem.getTrailerName());

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mTrailerData == null) {
            return 0;
        }
        return mTrailerData.size();
    }

    /**
     * Update the list of Trailer Items when new data has been laoded
     */
    public void swapData(ArrayList<TrailerItem> list) {
        mTrailerData = list;
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView trailerName;

        TrailerViewHolder(View view) {
            super(view);

            trailerName = (TextView) view.findViewById(R.id.trailerDescTextView);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            TrailerItem trailerItem = mTrailerData.get(getAdapterPosition());
            mClickHandler.onClick(trailerItem.getTrailerVideoKey());
        }
    }
}
