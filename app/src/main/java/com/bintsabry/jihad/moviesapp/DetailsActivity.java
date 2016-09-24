package com.bintsabry.jihad.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This inflates the layout for activity details_main
        setContentView(R.layout.details_main);

        Bundle extras = getIntent().getExtras();

        // This calls the fragment object which calls it's own onCreate > this inflates the layout of the fragment
        if (savedInstanceState == null) {
            DetailsFragment mDetailsFragment = new DetailsFragment();
            //Pass the "extras" Bundle that contains the selected movie to the fragment
            mDetailsFragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container,mDetailsFragment)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
