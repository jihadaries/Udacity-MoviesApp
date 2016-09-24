package com.bintsabry.jihad.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { MoviesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MoviesListFragment extends Fragment {
    private static final String GRID_INSTANCE_STATE = "MoviesGrid_State";
    /** // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
**/

    public MovieItemAdapter moviesAdapter;
    public GridView moviesGridView;
//    private OnFragmentInteractionListener mListener;
    MovieSelectListener movieListener;
    int mPosition;
    private static final String SELECTED_KEY = "selected_position";


    public MoviesListFragment() {
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
        args.putString(ARG_PARAM2, param);
        fragment.setArguments(args);
        return fragment;
    }**/

   public void setMovieSelectListener(MovieSelectListener movieSelectListener) {
       movieListener=movieSelectListener;
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        //setRetainInstance(true); //~to restore on rotate


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
        moviesAdapter=new MovieItemAdapter(  // the constructor parameters are context , layout resource, Arraylist of items
                getActivity(), // The current context (this activity)
                R.layout.item_movie, // The name of the layout ID that contains all components
                new ArrayList<MovieItem>() );


        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movies_grid, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        moviesGridView = (GridView) rootView.findViewById(R.id.mainGridView);
        moviesGridView.setAdapter(moviesAdapter);

        //set OnItemClickListener to the Grid View

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieItem selectedMovie = moviesAdapter.getItem(position);
                movieListener.setSelectedMovie(selectedMovie); // this passes the item to the listener to the activity
                //mPosition=position; //to get saved position
//                Intent intent = new Intent(getActivity(), DetailsActivity.class)
//                        .putExtra("MovieItem", selectedMovie);
//                startActivity(intent);
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }


        return rootView;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        getLoaderManager().initLoader(0, null, this);
//        super.onActivityCreated(savedInstanceState);
//    }
//

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movielist, menu);
        //inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this.getContext(), SettingsActivity.class));
            return true;
        }
        else if (id == R.id.action_sortby_popular) {
            mPosition=0;
            saveSortByPref("popular");
            loadMovies("popular");

            return true;
        }
        else if (id == R.id.action_sortby_toprated) {
            mPosition=0;
            saveSortByPref("top_rated");
            loadMovies("top_rated");
            return true;
        }
        else if (id == R.id.action_view_favorites) {
            mPosition=0;
            saveSortByPref("favorites");
            loadMovies("favorites");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveSortByPref(String sortby) {
        //save popular to shared pref
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_key_sortingorder), sortby);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        //loadMovies(null);
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadMovies(null);

//        MainActivity mainActivity = (MainActivity) getActivity();
//        if (mainActivity.mTwoPane && mainActivity.getSupportFragmentManager().findFragmentByTag("detailsFragment")==null ){
//            Toast.makeText(getContext(),"Two pane and detailsfragment not yet found", Toast.LENGTH_LONG).show();
//            moviesGridView.requestFocusFromTouch();
//            moviesGridView.setSelection(0);
//            moviesGridView.performItemClick(moviesGridView.getAdapter().getView(0 , null, null), 0, 0);
//
//        }


//        moviesGridView.requestFocusFromTouch();
//        moviesGridView.setSelection(0);
//        moviesGridView.performItemClick(moviesGridView.getAdapter().getView(0 , null, null), 0, 0);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        loadMovies(null);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mPosition!= moviesGridView.INVALID_POSITION){
          //  outState.putInt(SELECTED_KEY,mPosition);
            mPosition=moviesGridView.getFirstVisiblePosition();
            outState.putInt(SELECTED_KEY,mPosition);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onPause() {
        // Save grid view state @ onPause
        //state = moviesGridView.onSaveInstanceState();
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


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    /*
        Method to load movies.
        -Initiate AsyncTask
        ~-Get sorting order from shared preferences
        ~-Set location from preference
        -Call AsyncTask Execute
     */
    public void loadMovies(String sortBy) {
        FetchMoviesTask moviesTask = new FetchMoviesTask(this);

        //String sortBy = "popular"; //~ to get from settings
        //To get/read stored SharedPreference value
        if(sortBy==null){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sortBy = prefs.getString(getString(R.string.pref_key_sortingorder),getString(R.string.pref_sortingorder_default));
        }
        if (sortBy.equals("favorites")){
            loadFavMovies();
            return;
        }
        if(isOnline()){

            moviesTask.execute(sortBy);
        }
        else {
            Toast.makeText(getContext(), "There is no Internet connection. Please connect and try again.", Toast.LENGTH_LONG).show();

        }


    }

    private void loadFavMovies() {
        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(getActivity());

        //first get existing Set of fav movies
        Set<String> favMoviesSet = sharedPref.getStringSet(getString(R.string.pref_key_favoritemovies), null);
        if(favMoviesSet==null) { // return false if no fav movies yet
            Toast.makeText(getContext(), "There are no movies in Favorites", Toast.LENGTH_SHORT).show();
        }else{
            moviesAdapter.clear();
            //logic for adapter
            moviesAdapter.addAll(getMoviesFromJson(favMoviesSet));

        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private List<MovieItem> getMoviesFromJson(Set<String> moviesJsonSet) {
        List<MovieItem> mData = new ArrayList<MovieItem>();
        // These are the names of the JSON objects that need to be extracted.
        final String TMD_ID = "id";
        final String TMD_TITLE = "title";
        final String TMD_RELEASE_DATE = "release_date";
        final String TMD_POSTER_PATH = "poster_path";
        final String TMD_VOTE_AVERAGE = "vote_average";
        final String TMD_OVERVIEW = "overview";
        final String TMD_BACKDROP_PATH = "backdrop_path";
        try {
           // for(int i = 0; i < moviesJsonSet.size(); i++) {
            for (String s : moviesJsonSet) {
                    // Set Movies object
                MovieItem movieItem = new MovieItem() ;

                // Get the JSON object representing a movieJSON
                JSONObject movieJSON = new JSONObject(s);
                //movieJSON = moviesJsonSet.      moviesArray.getJSONObject(i);
                movieItem.setId(movieJSON.getString(TMD_ID));
                movieItem.setTitle(movieJSON.getString(TMD_TITLE));
                movieItem.setYear(movieJSON.getString(TMD_RELEASE_DATE).substring(0, 4));
                movieItem.setThumbnailPath(movieJSON.getString(TMD_POSTER_PATH));
                movieItem.setRating(movieJSON.getString(TMD_VOTE_AVERAGE));
                movieItem.setOverview(movieJSON.getString(TMD_OVERVIEW));
                movieItem.setBackdropPath(movieJSON.getString(TMD_BACKDROP_PATH));

                //Add it to movies list
                mData.add(movieItem);
            }
            } catch (JSONException e) {
                e.printStackTrace();
        }
        return mData;

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
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
//        public void onFragmentInteraction(Uri uri);
//    }
}
