package com.bintsabry.jihad.moviesapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
public class FetchDetailsTask extends AsyncTask<String, Void, List<Object>> {
    MovieDetailsAdapter detailsAdapter;
    final String LOG_TAG = FetchDetailsTask.class.getSimpleName();
    String MOVIES_BASE_URL;
    final String TRAILERS_PATH ="videos";
    final String REVIEWS_PATH ="reviews";
    final String APPID_PARAM = "api_key";


    public FetchDetailsTask(MovieDetailsAdapter movieDetailsAdapter) {
        this.detailsAdapter = movieDetailsAdapter;

    }

    @Override
    protected List<Object> doInBackground(String... params) { //get movie ID as param

        // Verify size of params.
        if (params.length == 0) {
            return null;
        }
        MOVIES_BASE_URL =detailsAdapter.getContext().getString(R.string.url_moviesapi_basepath);

        // Will contain the raw JSON responses as a string.
        String trailersJSONstr = null;
        String reviewsJSONstr = null;


        final String MOVIE_ID = params[0];

        //get trailersJSON
        trailersJSONstr=getTrailersJSON(MOVIE_ID);
        reviewsJSONstr=getReviewsJSON(MOVIE_ID);


        //Parse JSON string to objects
        try {
            return getDetailsListFromJson(trailersJSONstr,reviewsJSONstr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    public String getTrailersJSON(String movie_id){
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL for the themoviedb query
            // http://api.themoviedb.org/


            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(movie_id)
                    .appendPath(TRAILERS_PATH)
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
           return buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

            // If the code didn't successfully get the  data, there's no point in attempting
            // to parse it.

//            Toast.makeText(detailsAdapter.getContext(),  "Cannot connect to The Movies Database Trailers :( ", Toast.LENGTH_SHORT).show();
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

    }

    public String getReviewsJSON(String movie_id){
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL for the themoviedb query
            // http://api.themoviedb.org/


            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(movie_id)
                    .appendPath(REVIEWS_PATH)
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
            return buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

            // If the code didn't successfully get the  data, there's no point in attempting
            // to parse it.

            Toast.makeText(detailsAdapter.getContext(),  "Cannot connect to The Movies Database Reviews :( ", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    protected void onPostExecute(List<Object> result) {
        if (result != null) {
            detailsAdapter.clear();

                //logic for adapter
                detailsAdapter.addAll(result);

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
    private List<Object> getDetailsListFromJson(String trailersJsonStr, String reviewsJsonStr)
            throws JSONException {
        List<Object> mData = new ArrayList<Object>();
        // These are the names of the JSON objects that need to be extracted.
        final String TMD_RESULTS = "results";
        final String TMD_ID = "id";

        final String TMD_TRAILER_NAME = "name";
        final String TMD_TRAILER_KEY = "key";

        final String TMD_REVIEW_AUTHOR = "author";
        final String TMD_REVIEW_CONTENT = "content";

        // get all trailers
        JSONObject trailersResultJSON = new JSONObject(trailersJsonStr);
        JSONArray trailersArray = trailersResultJSON.getJSONArray( TMD_RESULTS);

        //set first item of mData as trailer heading if there are trailers
        if (trailersArray.length()!=0){
            ListHeaderItem trailerHeader = new ListHeaderItem();
            trailerHeader.setHeader("Trailers");
            mData.add(trailerHeader);
        }
        //set each trailer to a trailer item and add to list of objects in mData
        for(int i = 0; i < trailersArray.length(); i++) {
            // Set trailer object
            TrailerItem trailerItem = new TrailerItem() ;

            // Get the JSON object representing a trailerJSON
            JSONObject trailerJSON = trailersArray.getJSONObject(i);
            trailerItem.setId(trailerJSON.getString(TMD_ID));
            trailerItem.setName(trailerJSON.getString(TMD_TRAILER_NAME));
            trailerItem.setKey(trailerJSON.getString(TMD_TRAILER_KEY));

            //Add it to details list
            mData.add(trailerItem);


        }



        // get all reviews
        JSONObject reviewsResultJSON = new JSONObject(reviewsJsonStr);
        JSONArray reviewsArray = reviewsResultJSON.getJSONArray( TMD_RESULTS);

        //set first item of mData as reviews heading if there are reviews
        if (reviewsArray.length()!=0){
            ListHeaderItem reviewHeader = new ListHeaderItem();
            reviewHeader.setHeader("Reviews");
            mData.add(reviewHeader);
        }
        //set each review to a review item and add to list of objects in mData
        for(int i = 0; i < reviewsArray.length(); i++) {
            // Set trailer object
            ReviewItem reviewItem = new ReviewItem() ;

            // Get the JSON object representing a reviewJSON
            JSONObject reviewJSON = reviewsArray.getJSONObject(i);
            reviewItem.setId(reviewJSON.getString(TMD_ID));
            reviewItem.setAuthor(reviewJSON.getString(TMD_REVIEW_AUTHOR));
            reviewItem.setContent(reviewJSON.getString(TMD_REVIEW_CONTENT));


            //Add it to details list
            mData.add(reviewItem);


        }



        return mData;

    }

}
