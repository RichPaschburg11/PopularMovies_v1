package com.paschburg.rich.popularmovies_v1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;


public class MainActivity extends ActionBarActivity {

    public UserPrefs userPrefs;
    public Context context;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
           If the portrait screen width in dp is more than 180dp * 2, store a value of 0 as
           userPrefs.setWidth().
           For smaller portrait screen widths (in dp), save the screen width in pixels in
           userPrefs.setWidth().
           Refer to PopularMoviesFragment.java.  If userPrefs.getWidth() is 0, then the default
           value of 180dp will be used as column width and the number of columns will be set to auto_fit.
           If userPrefs.getWidth() is not 0, then the value divided by 2 will be used as the
           column width and the number of columns will be set to 2.  So each phone has at least two
           columns of movie images in portrait mode.  Note that some of each image is cut off, as the
           height is not changed
        */

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float heightdp = height / displayMetrics.density;
        float width = displayMetrics.widthPixels;
        float widthdp = width / displayMetrics.density;

        float size = width;
        if (size > height) size = height;
        float sizedp = widthdp;
        if (sizedp > heightdp) sizedp = heightdp;

        int intsize = (int)size;
        int intsizedp = (int)sizedp;

        if (intsizedp >= ( 180 * 2) )intsize = 0;
        else Log.e(LOG_TAG, "small screen size in dp = " + Integer.toString(intsizedp));

        context = getApplicationContext();
        UserPrefs userPrefs = new UserPrefs(context);
        userPrefs.setWidth(intsize);

    }
}
