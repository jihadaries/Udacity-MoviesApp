package com.bintsabry.jihad.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JihadSabry on 8/12/2016.
 */
public class MovieItemAdapter extends ArrayAdapter<MovieItem> {

    private final List<MovieItem> mData;
    private final Context mContext;

    public MovieItemAdapter(Context context, int resource, List<MovieItem> objects) {
        super(context, resource, objects);
        mData = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // here you are mapping the data to the view components
        //Create view
        LinearLayout root;
        ViewHolder vh;

        if(convertView==null) { // if an item placeholder doesn't exist create new
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            root = (LinearLayout) inflater.inflate(R.layout.item_movie, null);  // inflate the item layout
            vh = new ViewHolder();
            vh.title = (TextView) root.findViewById(R.id.item_movie_title); // map the textview to the layout id
            vh.year = (TextView) root.findViewById(R.id.item_movie_year);
            vh.thumbnail = (ImageView) root.findViewById(R.id.item_movie_thumbnail);

            root.setTag(vh);
        }else{
            root = (LinearLayout) convertView;
            vh = (ViewHolder) root.getTag();
        }

        //Get Item at position and map to item
        final MovieItem moveItem = mData.get(position);

        //Bind Data
        vh.title.setText(moveItem.getTitle());
        vh.year.setText(moveItem.getYear());
        //~ set image by picasso?
        Picasso.with(getContext())
                .load(mContext.getString(R.string.url_image_basepath)+moveItem.getThumbnailPath())
                .into(vh.thumbnail);

        return root;
    }

    class ViewHolder { // This contains your layout's XML comoponents
        TextView title;
        TextView year;
        ImageView thumbnail;
    }

}
