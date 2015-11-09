package com.paschburg.rich.popularmovies_v1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by richardpaschburg on 7/26/15.
 * Reference: JavaTechig at
 * http://javatechig.com/android/android-gridview-example-building-image-gallery-in-android
 */

public class GridViewAdapter extends ArrayAdapter<ImageItem> {
    private Context context;
    private int layoutResourceId;
    private ImageItem data[] = null;

    private static final String LOG_TAG = GridViewAdapter.class.getSimpleName();

    public GridViewAdapter(Context context, int layoutResourceId, ImageItem[] data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imagev = (ImageView)row.findViewById(R.id.imageres);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem imageItem = data[position];
        Picasso.with(this.context).load(imageItem.imagep).into(holder.imagev);

        return row;
    }

    static class ViewHolder {
        ImageView imagev;
    }

    @Override
    public void clear(){
        for (int i = 0; i < data.length; i++)
        data[i] = null;
    }


    public void addImageItem(int i, ImageItem imageitem){
        data[i] = imageitem;
    }
}
