package com.bintsabry.jihad.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity  implements MovieSelectListener{
    public boolean mTwoPane;
    GridView mainGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This inflates the layout for activity main
        setContentView(R.layout.activity_main);

        //Try to get the second pane from activity_main
        FrameLayout details_container = (FrameLayout) findViewById(R.id.details_container);

        if (null == details_container) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }

        // This calls the fragment object which calls it's own onCreate > this inflates the layout of the fragment
        if (savedInstanceState == null) {
            MoviesListFragment moviesListFragment = new MoviesListFragment();
            //Set interface  listener ~
            moviesListFragment.setMovieSelectListener(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, moviesListFragment,getString(R.string.fragment_moviesList))
                    .commit();
        }else{
            MoviesListFragment mlf = (MoviesListFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_moviesList));
            mlf.setMovieSelectListener(this);


        }






    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    @Override
//    protected void onResumeFragments(){
//        super.onResumeFragments();
//
//        MoviesListFragment mlf = (MoviesListFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_moviesList));
//        if ( null != mlf ) {
//            mlf.loadMovies(null);
//        }
//        DetailsFragment df = (DetailsFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_detailsMovie));
//        if ( null != df ) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.details_container,df,getString(R.string.fragment_detailsMovie)).commit();
//
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
        @Override
    public void setSelectedMovie(final MovieItem selectedMovie) {
        //Case Two Pane UI
        if (mTwoPane) {
            //new Handler().post(new Runnable() {
              //  public void run() {
                    DetailsFragment detailsFragment= new DetailsFragment();
                    Bundle extras= new Bundle();
                    extras.putSerializable("MovieItem", selectedMovie);
                    detailsFragment.setArguments(extras);
                    //getFragmentManager().beginTransaction().replace(R.id.details_container,detailsFragment).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.details_container,detailsFragment,getString(R.string.fragment_detailsMovie)).commit();

                //}
//            });
//            DetailsFragment df = (DetailsFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_detailsMovie));
//            if (df!=null){
//                //detailsFragment=df;
//                getSupportFragmentManager().beginTransaction().remove(df).commit();
//            }
//            Bundle extras= new Bundle();
//            extras.putSerializable("MovieItem", selectedMovie);
//            detailsFragment.setArguments(extras);
//            //getFragmentManager().beginTransaction().replace(R.id.details_container,detailsFragment).commit();
//            getSupportFragmentManager().beginTransaction().add(R.id.details_container,detailsFragment,getString(R.string.fragment_detailsMovie)).commit();

//            if ( null != detailsFragment ) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.details_container,detailsFragment,getString(R.string.fragment_detailsMovie)).commit();
//
//            }else{
//               getSupportFragmentManager().beginTransaction().add(R.id.details_container,detailsFragment,getString(R.string.fragment_detailsMovie)).commit();
//           }
       } else {
            //Case Single Pane UI
            Intent intent = new Intent(this, DetailsActivity.class)
            .putExtra("MovieItem", selectedMovie);
            startActivity(intent);
        }

    }
}
