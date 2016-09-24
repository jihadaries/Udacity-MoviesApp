package com.bintsabry.jihad.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
   /** // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
**/
   private MovieDetailsAdapter movieDetailsAdapter;
   private String selectedMovieID;
    private MovieItem selectedMovie;
    private ImageButton favBtn;


//    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesListFragment.
     */
   /** // TODO: Rename and change types and number of parameters
    public static MoviesListFragment newInstance(String param1, String param2) {
        MoviesListFragment fragment = new MoviesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }**/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setRetainInstance(true); //~to restore on rotate


        //setHasOptionsMenu(true);

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }**/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source and
        // use it to populate the grid/list view it's attached to.
        movieDetailsAdapter=new MovieDetailsAdapter(  // the constructor parameters are context , layout resource, Arraylist of items
                getActivity(), // The current context (this activity)
                0,
                new ArrayList<Object>() );

       // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_details_list, container, false);

        //View rootView =  inflater.inflate(R.layout.details_header, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        final ListView detailsListView = (ListView) rootView.findViewById(R.id.detail_list_view);

        ViewGroup detailsHeader = (ViewGroup) inflater.inflate(R.layout.details_header, detailsListView,
                false);

        detailsListView.addHeaderView(detailsHeader, null, false);

        // get the movie item from received bundles
        //Intent intent = getActivity().getIntent();
        //if (intent != null && intent.hasExtra("MovieItem")) {
          if(!getArguments().isEmpty()){
            //selectedMovie = (MovieItem) intent.getSerializableExtra("MovieItem");
            selectedMovie = (MovieItem) getArguments().getSerializable("MovieItem");
            selectedMovieID=selectedMovie.getId();
            //set header manually
            ((TextView) detailsHeader.findViewById(R.id.details_header_title)).setText(selectedMovie.getTitle());
            //~ set image by picasso
            Picasso.with(getContext())
                    .load(getContext().getString(R.string.url_image_basepath)+selectedMovie.getBackdropPath())
                    .into((ImageView) detailsHeader.findViewById(R.id.details_header_title_bgimage));

            ((TextView) detailsHeader.findViewById(R.id.details_header_year)).setText(selectedMovie.getYear());

            Picasso.with(getContext())
                    .load(getContext().getString(R.string.url_image_basepath)+selectedMovie.getThumbnailPath())
                    .into((ImageView) detailsHeader.findViewById(R.id.details_header_poster));

            ((TextView) detailsHeader.findViewById(R.id.details_header_rating)).setText(selectedMovie.getRating());
            ((TextView) detailsHeader.findViewById(R.id.details_header_overview)).setText(selectedMovie.getOverview());

            favBtn= ((ImageButton) detailsHeader.findViewById(R.id.details_header_favoritebtn));
            //Set Favorite image btn listener
           favBtn.setOnClickListener(setFavoritesListener);
            if(movieIsFav(selectedMovie)){
                favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
            }else {
                favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);

            }




        }

        // Get a reference to the ListView, and attach this adapter to it.
        detailsListView.setAdapter(movieDetailsAdapter);

        //set OnItemClickListener to the List View

        detailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // subtract headers count from position
                position -= detailsListView.getHeaderViewsCount();
                //Action to open youtube intent link if item is a trailer
                if(movieDetailsAdapter.getItem(position) instanceof TrailerItem ){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+((TrailerItem) movieDetailsAdapter.getItem(position)).getKey())));

                }
            }
        });

        return rootView;
    }



    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener setFavoritesListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked ~
            // Yes we will handle click here but which button clicked??? We don't know
            satFavorites(selectedMovie);

        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this.getContext(), SettingsActivity.class));
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadDetails(selectedMovieID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        loadDetails(selectedMovieID);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
    /*
        Method to load details.
        -Initiate AsyncTask
        ~-Get movies id
        -Call AsyncTask Execute
     */
    private void loadDetails(String movieID) {
        FetchDetailsTask detailsTask = new FetchDetailsTask(movieDetailsAdapter);
        //String sortBy = "popular"; //~ to get from settings
        //To get/read stored SharedPreference value
        if(movieID!=null){
            detailsTask.execute(movieID);
        }


    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
////            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    private void satFavorites(MovieItem favMovie){

        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(getActivity());
        //first get existing Set of fav movies
        Set<String> favMoviesSet = sharedPref.getStringSet(getString(R.string.pref_key_favoritemovies), null);
        if(favMoviesSet == null) {
            favMoviesSet = new HashSet<String>();
        }
        //convert selected movie to JSON object
        String favMovieJSONStr = convertMovieToJSON(selectedMovie);

        if(favMoviesSet.contains(favMovieJSONStr)){ //if movie exists then toggle fav and remove
            favMoviesSet.remove(favMovieJSONStr);
            favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            Toast.makeText(getActivity().getApplicationContext(), selectedMovie.getTitle()+" is removed from Favorites", Toast.LENGTH_SHORT).show();

        }else{ // add as fave
            favMoviesSet.add(favMovieJSONStr);
            favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
            Toast.makeText(getActivity().getApplicationContext(), selectedMovie.getTitle()+" is now a favorite! <3", Toast.LENGTH_SHORT).show();

        }

        //save updated list to shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(getString(R.string.pref_key_favoritemovies), favMoviesSet);
        editor.commit();


    }

    private boolean movieIsFav(MovieItem selctMovie) {
        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(getActivity());

        //first get existing Set of fav movies
        Set<String> favMoviesSet = sharedPref.getStringSet(getString(R.string.pref_key_favoritemovies), null);
        if(favMoviesSet == null) { // return false if no fav movies yet
            return false;
        }
        //convert selected movie to JSON object
        String favMovieJSONStr = convertMovieToJSON(selctMovie);

        if(favMoviesSet.contains(favMovieJSONStr)){ //if movie exists then return true
            return true;
        }else{ // if not exist return false
             return false;
        }
    }

    private String convertMovieToJSON(MovieItem selectedMovie) {
        final String TMD_ID = "id";
        final String TMD_TITLE = "title";
        final String TMD_RELEASE_DATE = "release_date";
        final String TMD_POSTER_PATH = "poster_path";
        final String TMD_VOTE_AVERAGE = "vote_average";
        final String TMD_OVERVIEW = "overview";
        final String TMD_BACKDROP_PATH = "backdrop_path";

        JSONObject movieJSONObj = new JSONObject();
        try {
            movieJSONObj.put(TMD_ID, selectedMovie.getId());
            movieJSONObj.put(TMD_TITLE,selectedMovie.getTitle());
            movieJSONObj.put(TMD_RELEASE_DATE,selectedMovie.getYear());
            movieJSONObj.put(TMD_POSTER_PATH,selectedMovie.getThumbnailPath());
            movieJSONObj.put(TMD_VOTE_AVERAGE,selectedMovie.getRating());
            movieJSONObj.put(TMD_OVERVIEW,selectedMovie.getOverview());
            movieJSONObj.put(TMD_BACKDROP_PATH,selectedMovie.getBackdropPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieJSONObj.toString();
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//
//       public void onFragmentInteraction(Uri uri);
//    }
}
