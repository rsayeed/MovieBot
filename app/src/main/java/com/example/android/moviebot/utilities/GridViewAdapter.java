package com.example.android.moviebot.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.moviebot.R;
import com.example.android.moviebot.model.GridItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/** This adapter class is required to render the collection of data items (the movie poster images
 *  that we will obtain from themoviedb API.
 *
 * Created by rubab on 4/9/17.
 */

public class GridViewAdapter extends ArrayAdapter<GridItem> {

    private static final String TAG = GridViewAdapter.class.getSimpleName();


    private Context mContext;
    private int layoutResourceId;
    private List<GridItem> mGridData = new ArrayList<GridItem>();

    /**
     * Constructor requires id of grid item layout and list of data to operate on
     *
     * @param mContext
     * @param layoutResourceId
     * @param mGridData
     */
    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {

        super(mContext, layoutResourceId, mGridData);

        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.mGridData = mGridData;
    }

    /**
     * Updates grid data and refresh grid items
     * @param mGridData
     */
    public void setGridData(List<GridItem> mGridData) {

        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (null == mGridData) return 0;
        return mGridData.size();
    }

    @Nullable
    @Override
    public GridItem getItem(int position) {

       return mGridData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) row.findViewById(R.id.grid_movie_poster);
            row.setTag(holder);

        } else {

            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData.get(position);

        // Download image from url and display it onto the imageview
        Picasso.with(mContext).load(item.getImage()).into(holder.mImageView);
        return row;
    }

    class ViewHolder {

        ImageView mImageView;
    }
}
