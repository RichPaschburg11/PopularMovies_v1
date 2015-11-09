package com.paschburg.rich.popularmovies_v1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG = Fragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        String movieText = "";
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.movie_information, container, false);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieIndex = intent.getStringExtra(Intent.EXTRA_TEXT);

            UserPrefs userPrefs = new UserPrefs(getActivity());
            movieText = userPrefs.getString(movieIndex);

        }

        String[] details = new String[6];

        try {

            details = getMovieDetailsFromJson(movieText);

        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();

        }

        ((TextView) rootView.findViewById(R.id.title)).setText(details[0]);

        // ((TextView) rootView.findViewById(R.id.title)).setText(movieText);
        Picasso.with(getActivity()).load(details[1]).into((ImageView) rootView.findViewById(R.id.image_for_details));

        ((TextView) rootView.findViewById(R.id.year)).setText(details[2]);
        ((TextView) rootView.findViewById(R.id.votes)).setText(details[3]);
        ((TextView) rootView.findViewById(R.id.popularity)).setText(details[4]);
        ((TextView) rootView.findViewById(R.id.description)).setText(details[5]);

        return rootView;

    }

    private String[] getMovieDetailsFromJson(String jsonString)
            throws JSONException {

        final String TMDB_MOVIES = "results";
        final String TMDB_IMAGE = "poster_path";
        final String TMDB_TITLE = "title";
        final String TMDB_RELEASEDATE = "release_date";
        final String TMDB_POPULARITY = "popularity";
        final String TMDB_VOTEAVERAGE = "vote_average";
        final String TMDB_DESCRIPTION = "overview";

        Uri.Builder builder;
        String movieName;
        String releasedate;
        String voteaverage;
        String popularity;
        String description;
        String image;
        String[] details = new String[6];

        JSONObject movieObject = new JSONObject(jsonString);

        movieName = movieObject.getString(TMDB_TITLE);
        if (movieName.equals("null")) movieName = "";
        details[0] = movieName;

        image = movieObject.getString(TMDB_IMAGE);
        builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath("")
                .appendPath(image.substring(1));
        details[1] = builder.build().toString();

        releasedate = movieObject.getString(TMDB_RELEASEDATE);
        if (releasedate.equals("null")) releasedate = "    ";
        if (releasedate.equals("")) releasedate = "    ";
        details[2] = releasedate.substring(0,4);

        voteaverage = movieObject.getString(TMDB_VOTEAVERAGE);
        if (voteaverage.equals("null")){
            details[3] = "";
        }
        else details[3] = "vote " + voteaverage + "/10";

        popularity = movieObject.getString(TMDB_POPULARITY);
        // Log.e("popularity = ", popularity.substring(0,5));
        if (popularity.equals("null")){
            details[4] = "";
        }
        else if (popularity.contains("E-")) {
            NumberFormat formatter = new DecimalFormat("#0.000000");
            Double popularity2 = Double.parseDouble(popularity);
            String popularity3 = formatter.format(popularity2);
            details[4] = "popularity is low (" + popularity3 +")";
        }
        else {
            if (popularity.length() < 6) popularity = popularity + "     ";
            details[4] = "popularity " + popularity.substring(0,5);
        }


        description = movieObject.getString(TMDB_DESCRIPTION);
        if (description.equals("null")) description = "";
        details[5] = description;

        return details;
    }
}
