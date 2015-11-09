package com.paschburg.rich.popularmovies_v1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    public UserPrefs userPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPrefs = new UserPrefs(getApplicationContext());
    }
}
