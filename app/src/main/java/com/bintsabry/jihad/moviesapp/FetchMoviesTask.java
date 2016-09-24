package com.bintsabry.jihad.moviesapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JihadSabry on 9/13/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<MovieItem>> {
    MovieItemAdapter moviesAdapter;
    MainActivity srcActivity;
    MoviesListFragment srcFragment;
    //Creating constructor with movieAdapter reference
//    public FetchMoviesTask(MovieItemAdapter m){
//        moviesAdapter=m;
//    }

    public FetchMoviesTask(MoviesListFragment f){
        srcFragment=f;
        moviesAdapter=srcFragment.moviesAdapter;
    }


    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    @Override
    protected List<MovieItem> doInBackground(String... params) {

        // Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJSONstr = null;

        try {
            // Construct the URL for the themoviedb query
            // http://api.themoviedb.org/
            final String MOVIES_BASE_URL =
                    srcFragment.getString(R.string.url_moviesapi_basepath);
            final String SORTING_ORDER = params[0];
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(SORTING_ORDER)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.THEMOVIEDB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to theMovieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJSONstr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

            // If the code didn't successfully get the  data, there's no point in attempting
            // to parse it.

//            Toast.makeText(srcFragment.getContext(),  "Cannot connect to the Internet :( ", Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        //Parse JSON string to objects
        try {
            return getMoviesFromJson(moviesJSONstr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(List<MovieItem> result) {
        if (result != null) {
            moviesAdapter.clear();
                //logic for adapter
            moviesAdapter.addAll(result);

             MainActivity mainActivity = (MainActivity) srcFragment.getActivity();
                if (mainActivity.mTwoPane && mainActivity.getSupportFragmentManager().findFragmentByTag("detailsFragment")==null ){
                    //  Toast.makeText(srcFragment.getContext(),"Two pane and detailsfragment not yet found", Toast.LENGTH_LONG).show();
                srcFragment.moviesGridView.requestFocusFromTouch();
                srcFragment.moviesGridView.setSelection(0);
                srcFragment.moviesGridView.performItemClick(srcFragment.moviesGridView.getAdapter().getView(0 , null, null), 0, 0);

            }


                //moviesAdapter..swapCursor(data);
                if (srcFragment.mPosition!= srcFragment.moviesGridView.INVALID_POSITION) {
                    // If we don't need to restart the loader, and there's a desired position to restore
                    // to, do so now.
                    srcFragment.moviesGridView.smoothScrollToPosition(srcFragment.mPosition);
                }





            // New data is back from the server.  Hooray!
        }
    }


    /**
     * Take the String representing the complete movies list in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private List<MovieItem> getMoviesFromJson(String moviesJsonStr)
            throws JSONException {
        List<MovieItem> mData = new ArrayList<MovieItem>();
        // These are the names of the JSON objects that need to be extracted.
        final String TMD_RESULTS = "results";
        final String TMD_ID = "id";
        final String TMD_TITLE = "title";
        final String TMD_RELEASE_DATE = "release_date";
        final String TMD_POSTER_PATH = "poster_path";
        final String TMD_VOTE_AVERAGE = "vote_average";
        final String TMD_OVERVIEW = "overview";
        final String TMD_BACKDROP_PATH = "backdrop_path";


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray( TMD_RESULTS);

        // TMD returns Movies list based upon the sorting order setting


        for(int i = 0; i < moviesArray.length(); i++) {
            // Set Movies object
            MovieItem movieItem = new MovieItem() ;

            // Get the JSON object represen
            // ting a movieJSON
            JSONObject movieJSON = moviesArray.getJSONObject(i);
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
        return mData;

    }

}
