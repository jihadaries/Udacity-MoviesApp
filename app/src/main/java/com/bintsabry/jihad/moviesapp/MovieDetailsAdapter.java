package com.bintsabry.jihad.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JihadSabry on 8/12/2016.
 */
public class MovieDetailsAdapter extends ArrayAdapter<Object> {

    private final List<Object> mData;
    private final Context mContext;

    // Setting type flags
    private static final int TYPE_LIST_HEADER = 0;
    private static final int TYPE_TRAILER = 1;
    private static final int TYPE_REVIEW = 2;

    public MovieDetailsAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
        mData = objects;
        mContext = context;
    }

    @Override
    public int getViewTypeCount() { //This method returns the number of types of Views that will be created by getView method.
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position) instanceof ListHeaderItem){
            return 0;
        }
        else if(mData.get(position) instanceof TrailerItem){
            return 1;
        }
        else if(mData.get(position) instanceof ReviewItem){
            return 2;
        }

        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // here you are mapping the data to the view components
        //Create view
        LinearLayout root;
        ViewHolder vh;
        // get object type
        int objectType = getItemViewType(position);
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null) { // if an item placeholder doesn't exist create new
            vh = new ViewHolder();
            switch (objectType){
                case TYPE_LIST_HEADER:
                    convertView= (LinearLayout) inflater.inflate(R.layout.item_list_header, null);  // inflate the item layout
                    //root= (LinearLayout) inflater.inflate(R.layout.item_list_header, null);  // inflate the item layout
                    vh.list_header= (TextView) convertView.findViewById(R.id.item_list_header);
                    convertView.setOnClickListener(null);

                    break;

                case TYPE_TRAILER:
                    convertView= (LinearLayout) inflater.inflate(R.layout.item_trailer, null);  // inflate the item layout
                    //root= (LinearLayout) inflater.inflate(R.layout.item_trailer, null);  // inflate the item layout
                    vh.trailer_name= (TextView) convertView.findViewById(R.id.item_trailer_name);
//                    vh.trailer_playbtn= (ImageButton) convertView.findViewById(R.id.item_trailer_playbtn);
                    break;

                case TYPE_REVIEW:
                    convertView= (LinearLayout) inflater.inflate(R.layout.item_review, null);  // inflate the item layout
                    //root= (LinearLayout) inflater.inflate(R.layout.item_review, null);  // inflate the item layout
                    vh.review_author= (TextView) convertView.findViewById(R.id.item_review_author);
                    vh.review_content= (TextView) convertView.findViewById(R.id.item_review_content);
                    break;
            }
            convertView.setTag(vh);
            //root.setTag(vh);
        }else{
            //root = (LinearLayout) convertView;
            vh = (ViewHolder) convertView.getTag();
        }

        switch (objectType){
            case TYPE_LIST_HEADER:
                ListHeaderItem listHeaderItem = new ListHeaderItem();
                //Get Item at position and map to item
                    listHeaderItem = (ListHeaderItem) mData.get(position);
                //Bind Data
                vh.list_header.setText(listHeaderItem.getHeader());
                break;

            case TYPE_TRAILER:
                TrailerItem trailerItem = new TrailerItem();
                trailerItem = (TrailerItem) mData.get(position);
                vh.trailer_name.setText(trailerItem.getName());
                break;

            case TYPE_REVIEW:
                ReviewItem reviewItem = new ReviewItem();
                reviewItem = (ReviewItem) mData.get(position);
                vh.review_author.setText(reviewItem.getAuthor());
                vh.review_content.setText(reviewItem.getContent());
                break;
        }



        return convertView;
    }

    class ViewHolder { // This contains your layout's XML comoponents
        TextView trailer_name;
        TextView review_author;
        TextView review_content;
        TextView list_header;
        ImageButton trailer_playbtn;


    }

}
